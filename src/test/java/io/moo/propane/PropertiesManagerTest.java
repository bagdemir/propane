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
package io.moo.propane;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import org.junit.Test;

import io.moo.propane.annotation.TestProps;
import io.moo.propane.exception.InvalidPropsEntityException;

/**
 * Unit test for properties manager.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class PropertiesManagerTest {

  @Test(expected = InvalidPropsEntityException.class)
  public void testIsRegisteredTestUsingInvalidPropsEntity() {
    final PropertiesManager propertiesManager = new PropertiesManagerImpl();
    propertiesManager.register(Object.class);
  }


  @Test
  public void testIsRegisteredTestUsingValidPropsEntityButAlreadyRegistered() {
    final PropertiesManager propertiesManager = new PropertiesManagerImpl();
    assertThat(propertiesManager.register(TestProps.class), equalTo(true));
    assertThat(propertiesManager.register(TestProps.class), equalTo(false));
  }


  @Test
  public void testIsRegisteredTestUsingValidPropsEntity() {
    final PropertiesManager propertiesManager = new PropertiesManagerImpl();
    propertiesManager.register(TestProps.class);
    assertThat(propertiesManager.isRegistered(TestProps.class), equalTo(true));
  }


  @Test
  public void testLoad() {
    final PropertiesManager propertiesManager = new PropertiesManagerImpl();
    propertiesManager.register(TestProps.class);
    final Optional<TestProps> props = propertiesManager.load(TestProps.class);
    assertThat(props.isPresent(), equalTo(true));
    final TestProps testProps = props.get();
    assertThat(testProps.getUrl(), equalTo("http://localhost/"));
    assertThat(testProps.getTimeout(), equalTo(1000L));
    assertThat(testProps.getCount(), equalTo(99));
  }
}
