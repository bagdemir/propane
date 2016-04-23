package io.moo.propane;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.moo.propane.data.ConfigurationEntity;

/**
 * Tests for parsing YAML configuration.
 */
public class ConfigurationYamlParserTest {

  private static final String CONFIGURATIONS_TEST1_YML =
          "configurations/test1.yml";

  private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());


  @Test
  public void testParseYml() {


    try (InputStreamReader reader = new InputStreamReader(getClass()
            .getClassLoader().getResourceAsStream(CONFIGURATIONS_TEST1_YML))) {

      JsonNode jsonNode = mapper.readTree(reader);
      List<ConfigurationEntity> entities = new ArrayList<>();
      parse(jsonNode, new TreeSet<>(), entities);
    }
    catch (final IOException e1) {
      e1.printStackTrace();
    }
  }


  private void parse(final JsonNode jsonNode,
                     final Set<String> contextIds,
                     final List<ConfigurationEntity> entities) {

    final Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields();

    while (it.hasNext()) {
      final Map.Entry<String, JsonNode> next = it.next();
      if (next.getValue().isContainerNode()) {
        contextIds.add(next.getKey());
        parse(next.getValue(), contextIds, entities);
      } else {
        entities.add(new ConfigurationEntity("test1", contextIds, next
                .getKey(),
                next.getValue().asText()));
      }
    }
  }
}
