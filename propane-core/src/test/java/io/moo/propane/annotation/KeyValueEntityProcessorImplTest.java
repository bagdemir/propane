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
package io.moo.propane.annotation;

import com.google.common.collect.ImmutableMap;
import io.moo.propane.annotation.processor.AnnotationProcessor;
import io.moo.propane.annotation.processor.ConfigurationAnnotationProcessorImpl;
import io.moo.propane.sources.ConfigData;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class KeyValueEntityProcessorImplTest {
  public static final String COMPONENT_ID = "test";
  public static final long EXPECTED_TIMEOUT = 100L;
  public static final int EXPECTED_COUNT = 1;
  public static final String EXPECTED_URL = "abc";


  @Test
  public void testCreate() {
    final Map<String, String> propsMap = ImmutableMap.of(
            "testProp", "abc",
            "longProp", "100",
            "intProp", "1"
    );
    final ConfigData configData = new ConfigData();
    configData.setSource("classpath://configurations/test1.properties");
    configData.setPropsMap(propsMap);

    AnnotationProcessor processor = new ConfigurationAnnotationProcessorImpl();
    Test1ConfigEntity testPropsWithClasspathSource = processor.createEntity(Test1ConfigEntity.class, configData);

    assertThat(testPropsWithClasspathSource, notNullValue());
    assertThat(testPropsWithClasspathSource.getTimeout(), equalTo(EXPECTED_TIMEOUT));
    assertThat(testPropsWithClasspathSource.getCount(), equalTo(EXPECTED_COUNT));
    assertThat(testPropsWithClasspathSource.getUrl(), equalTo(EXPECTED_URL));
  }
}
