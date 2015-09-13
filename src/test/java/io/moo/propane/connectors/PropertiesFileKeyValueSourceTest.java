/**
 *
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 moo.io , Erhan Bagdemir
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 *
 */
package io.moo.propane.connectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class PropertiesFileKeyValueSourceTest {
  public static final int EXPECTED_NUM_OF_CONFIGS = 3;
  private File file;

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();


  @Before
  public void setUp() throws IOException {
    file = folder.newFile("testConfig.properties");

    Map<String, Object> testProps = new HashMap<>(3);

    testProps.put("io.moo.test.component/longProp", "1000");
    testProps.put("io.moo.test.component/testProp", "http://localhost/");
    testProps.put("io.moo.test.component/intProp", "99");

    Properties properties = new Properties();
    properties.putAll(testProps);
    properties.store(new FileWriter(file), "Test properties");

  }


  @Test
  public void testRead() throws IOException {
    final PropertiesFileConfigurationSource source =
            new PropertiesFileConfigurationSource(file.getAbsolutePath());

    final ConfigData configData = source.read();
    Map<String, String> propsMap = configData.getPropsMap();
    assertThat(propsMap, notNullValue());
    assertThat(propsMap.size(), equalTo(EXPECTED_NUM_OF_CONFIGS));
  }


  @After
  public void tearDown() {
    if (!file.delete())
      throw new RuntimeException("File could not be deleted.");
  }
}
