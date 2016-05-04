/**
 * The MIT License (MIT) <p> Copyright (c) 2015 moo.io , Erhan Bagdemir <p> Permission is hereby
 * granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions: <p> The above copyright notice and this permission
 * notice shall be included in all copies or substantial portions of the Software. <p> THE SOFTWARE
 * IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.moo.propane.sources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import io.moo.propane.data.ConfigurationEntity;
import io.moo.propane.extractors.DefaultComponentIdExtractor;
import io.moo.propane.extractors.TokenExtractor;

import static io.moo.propane.annotation.processor.AnnotationUtils.getDefinedProperties;


/**
 * {@link ClasspathFileConfigurationSource} uses classpath resources as sources.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class ClasspathFileConfigurationSource extends ConfigurationSource {
    private static final Logger LOG = LogManager.getLogger();

    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private final TokenExtractor componentIdExtractor = new
            DefaultComponentIdExtractor();


    public ClasspathFileConfigurationSource(
            final String source,
            final Class<?> entityType) {
        super(source, entityType);
    }


    @Override
    public Optional<ConfigData> read(final Class clazz) {

        try (InputStreamReader reader = new InputStreamReader(getClass()
                .getClassLoader().getResourceAsStream(source))) {

            List<ConfigurationEntity> entities = new ArrayList<>();

            parse(getSource(), mapper.readTree(reader), new ArrayList<>(),
                    entities, 0);

            return Optional.of(new ConfigData(getSource(), entities));

        } catch (IOException e) {
            LOG.error(e);
            return Optional.empty();
        }
    }


    private void parse(
            final String source,
            final JsonNode jsonNode,
            final List<String> contextIds,
            final List<ConfigurationEntity> entities,
            final int level) {

        final Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields();
        final List<String> definedProperties = getDefinedProperties(getEntityType());

        while (it.hasNext()) {

            Map.Entry<String, JsonNode> next = it.next();
            String key = next.getKey();
            JsonNode value = next.getValue();

            if (!definedProperties.contains(key) && value.isContainerNode()) {

                if (level == 0) {
                    final List<String> ids = new ArrayList<>();
                    ids.add(key);
                    parse(source, value, ids, entities, level + 1);
                } else {
                    contextIds.add(key);
                    parse(source, value, contextIds, entities, level);
                }

            } else if (definedProperties.contains(key)) {

                String componentName = componentIdExtractor.extract(source).iterator().next();
                Set<String> contexts = new TreeSet<>(contextIds);
                ConfigurationEntity configurationEntity = new ConfigurationEntity(
                        componentName,
                        contexts,
                        key,
                        map(value));

                LOG.info("Creating entity: {}", configurationEntity);

                entities.add(configurationEntity);
            }
        }
    }

    private Object map(final JsonNode input) {
        if (input.isArray()) {
            Iterator<JsonNode> elements = input.elements();
            List<Object> values = new ArrayList<>();
            while (elements.hasNext()) {
                Optional.of(elements.next()).
                        filter(JsonNode::isValueNode).
                        map(JsonNode::asText).
                        ifPresent(values::add);
            }
            return values;
        } else if (input.isObject()) {
            return input.asText();
        } else if (input.isValueNode()) {
            return input.asText();
        } else {
            return input;
        }
    }
}
