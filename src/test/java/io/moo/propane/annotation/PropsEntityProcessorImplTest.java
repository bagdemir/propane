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
package io.moo.propane.annotation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import io.moo.propane.annotation.processor.AnnotationProcessor;
import io.moo.propane.annotation.processor.PropsEntityAnnotationProcessorImpl;
import io.moo.propane.data.PropertiesEntity;

/**
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class PropsEntityProcessorImplTest {

  @Test
  public void testCreate() {

    final PropertiesEntity propStr = new PropertiesEntity();
    propStr.setComponentId("io.moo.test.component");
    propStr.setContextIds(new String[]{"test"});
    propStr.setPropertyName("testProp");
    propStr.setPropertyValue("abc");

    final PropertiesEntity propLong = new PropertiesEntity();
    propLong.setComponentId("io.moo.test.component");
    propLong.setContextIds(new String[]{"test"});
    propLong.setPropertyName("longProp");
    propLong.setPropertyValue(100L);

    final PropertiesEntity propInt = new PropertiesEntity();
    propInt.setComponentId("io.moo.test.component");
    propInt.setContextIds(new String[]{"test"});
    propInt.setPropertyName("intProp");
    propInt.setPropertyValue(1);

    final Collection<PropertiesEntity> props = ImmutableList.of(propStr, propLong, propInt);
    final AnnotationProcessor processor = new PropsEntityAnnotationProcessorImpl();
    final TestProps testProps = processor.createEntity(TestProps.class, props);
    assertThat(testProps, notNullValue());
    assertThat(testProps.getTimeout(), equalTo(100L));
    assertThat(testProps.getCount(), equalTo(1));
    assertThat(testProps.getUrl(), equalTo("abc"));
  }
}
