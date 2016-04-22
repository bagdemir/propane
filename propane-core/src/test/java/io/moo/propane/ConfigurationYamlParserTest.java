package io.moo.propane;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.moo.propane.data.ConfigurationEntity;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by bagdemir on 06/02/16.
 */
public class ConfigurationYamlParserTest {
  private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

  @Test
  public void testParseYml() {


    try (InputStreamReader reader = new InputStreamReader(getClass()
            .getClassLoader().getResourceAsStream("configurations/test1.yml")
    )) {

      JsonNode jsonNode = mapper.readTree(reader);

      List<ConfigurationEntity> entities = new ArrayList<>();
      parse(jsonNode, new TreeSet<>(), entities);
      System.out.println(entities);


    } catch (IOException e1) {
      e1.printStackTrace();
    }

  }

  private void parse(final JsonNode jsonNode, final Set<String>
          contextIds, final List<ConfigurationEntity> entities) {

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
