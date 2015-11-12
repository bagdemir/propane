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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import io.moo.propane.data.ConfigurationEntity;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import io.moo.propane.annotation.processor.AnnotationProcessor;
import io.moo.propane.annotation.processor.PropsAnnotationProcessorImpl;

/**
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class KeyValueEntityProcessorImplTest {
    public static final String COMPONENT_ID = "testConfig";
    public static final long EXPECTED_TIMEOUT = 100L;
    public static final int EXPECTED_COUNT = 1;
    public static final String EXPECTED_URL = "abc";

    @Test
    public void testCreate() {

        final ConfigurationEntity propStr = new ConfigurationEntity(COMPONENT_ID,
                new String[]{"test"},
                "testProp",
                "abc");

        final ConfigurationEntity propLong = new ConfigurationEntity(COMPONENT_ID,
                new String[]{"test"},
                "longProp",
                "100");

        final ConfigurationEntity propInt = new ConfigurationEntity(COMPONENT_ID,
                new String[]{"test"},
                "intProp",
                "1");

        List<ConfigurationEntity> props = ImmutableList.of(propStr, propLong, propInt);
        AnnotationProcessor processor = new PropsAnnotationProcessorImpl();
        TestPropsWithClasspathSource testPropsWithClasspathSource = processor.createEntity(TestPropsWithClasspathSource.class, props);

        assertThat(testPropsWithClasspathSource, notNullValue());
        assertThat(testPropsWithClasspathSource.getTimeout(), equalTo(EXPECTED_TIMEOUT));
        assertThat(testPropsWithClasspathSource.getCount(), equalTo(EXPECTED_COUNT));
        assertThat(testPropsWithClasspathSource.getUrl(), equalTo(EXPECTED_URL));
    }
}
