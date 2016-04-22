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


import io.moo.propane.annotation.processor.AnnotationProcessor;
import io.moo.propane.annotation.processor.ConfigurationAnnotationProcessorImpl;
import io.moo.propane.data.ConfigurationEntity;
import io.moo.propane.sources.ConfigData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    final List<ConfigurationEntity> entitites = new ArrayList<>();
    entitites.add(new ConfigurationEntity("test1", Collections.emptySet(), "testProp",
                    "abc"));
    entitites.add(new ConfigurationEntity("test1", Collections.emptySet(), "intProp",
                    "1"));
    entitites.add(new ConfigurationEntity("test1", Collections.emptySet(), "longProp",
                    "100")
    );
    final String source = "classpath://configurations/test1.yml";
    final ConfigData configData = new ConfigData(source, entitites);

    AnnotationProcessor processor = new ConfigurationAnnotationProcessorImpl();
    Test1ConfigEntity testPropsWithClasspathSource = processor.createEntity(Test1ConfigEntity.class, configData);

    assertThat(testPropsWithClasspathSource, notNullValue());
    assertThat(testPropsWithClasspathSource.getTimeout(), equalTo(EXPECTED_TIMEOUT));
    assertThat(testPropsWithClasspathSource.getCount(), equalTo(EXPECTED_COUNT));
    assertThat(testPropsWithClasspathSource.getUrl(), equalTo(EXPECTED_URL));
  }
}
