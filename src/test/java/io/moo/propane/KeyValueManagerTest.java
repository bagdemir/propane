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
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import org.junit.Test;

import io.moo.propane.annotation.TestPropsWithClasspathSource;
import io.moo.propane.exception.InvalidConfigurationEntityException;

/**
 * Unit test for properties manager.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class KeyValueManagerTest {

  @Test(expected = InvalidConfigurationEntityException.class)
  public void testIsRegisteredTestUsingInvalidPropsEntity() {
    final ConfigurationManager configurationManager = new ConfigurationManagerImpl();
    configurationManager.register(Object.class);
  }


  @Test
  public void testIsRegisteredTestUsingValidPropsEntityButAlreadyRegistered() {
    final ConfigurationManager configurationManager = new ConfigurationManagerImpl();
    assertThat(configurationManager.register(TestPropsWithClasspathSource.class), equalTo(true));
    assertThat(configurationManager.register(TestPropsWithClasspathSource.class), equalTo(false));
  }


  @Test
  public void testIsRegisteredTestUsingValidPropsEntity() {
    final ConfigurationManager configurationManager = new ConfigurationManagerImpl();
    configurationManager.register(TestPropsWithClasspathSource.class);
    assertThat(configurationManager.isRegistered(TestPropsWithClasspathSource.class), equalTo(true));
  }


  @Test
  public void testLoad() throws InterruptedException {
    final ConfigurationManager configurationManager = new ConfigurationManagerImpl();
    configurationManager.register(TestPropsWithClasspathSource.class);

    Thread.sleep(100L);
    final Optional<TestPropsWithClasspathSource> props = configurationManager.load(TestPropsWithClasspathSource.class);
    assertThat(props.isPresent(), equalTo(true));

    final TestPropsWithClasspathSource testPropsWithClasspathSource = props.get();
    assertThat(testPropsWithClasspathSource.getUrl(), equalTo("http://localhost/"));
    assertThat(testPropsWithClasspathSource.getTimeout(), equalTo(1000L));
    assertThat(testPropsWithClasspathSource.getCount(), equalTo(99));
  }
}
