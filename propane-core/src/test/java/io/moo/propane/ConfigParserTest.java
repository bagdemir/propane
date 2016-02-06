/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2016 moo.io , Erhan Bagdemir
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.moo.propane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class ConfigParserTest {

  @Test
  public void testParseConfig() {

    try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream
            ("/configurations/testconfig1.yml"))) {

      YAMLFactory factory = new YAMLFactory();
      JsonParser parser = factory.createParser(reader);

      while (parser.nextValue() != null) {

        System.out.println(parser.getCurrentName() + " : " + parser
                .getCurrentValue() + " : " + parser.getText());
      }

    }
    catch (IOException e) {
      System.out.println(e);
    }
  }
}
