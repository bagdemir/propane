/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 moo.io , Erhan Bagdemir
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.moo.propane.sources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.moo.propane.data.ConfigurationEntity;
import io.moo.propane.extractors.DefaultComponentIdExtractor;
import io.moo.propane.extractors.TokenExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


/**
 * {@link ClasspathFileConfigurationSource} uses classpath resources as
 * sources.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class ClasspathFileConfigurationSource extends ConfigurationSource {
  private static final Logger LOG = LogManager.getLogger();
  //TODO need of a singleton.
  private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
  private final TokenExtractor componentIdExtractor = new
          DefaultComponentIdExtractor();

  public ClasspathFileConfigurationSource(final String source) {
    super(source);
  }


  @Override
  public Optional<ConfigData> read() {

    try (InputStreamReader reader = new InputStreamReader(getClass()
            .getClassLoader().getResourceAsStream(source))) {

      final List<ConfigurationEntity> entities = new ArrayList<>();
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

    while (it.hasNext()) {
      final Map.Entry<String, JsonNode> next = it.next();
      if (next.getValue().isContainerNode()) {

        if (level == 0) {
          final List<String> ids = new ArrayList<>();
          ids.add(next.getKey());
          parse(source, next.getValue(), ids, entities, level + 1);
        } else {
          contextIds.add(next.getKey());
          parse(source, next.getValue(), contextIds, entities, level);
        }

      } else {
        entities.add(new ConfigurationEntity(componentIdExtractor.extract
                (source).iterator().next(), new
                ArrayList<String>
                (contextIds), next
                .getKey(),
                next.getValue().asText()));
      }
    }
  }
}
