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
package io.moo.propane;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import io.moo.propane.annotation.Test1ConfigEntity;
import io.moo.propane.data.ContextInfo;
import io.moo.propane.exception.InvalidConfigurationEntityException;

/**
 * Unit test for the configuration manager.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class ConfigurationManagerTest {
  private static final Logger LOG = LogManager.getLogger();
  private static final String TEST_PROPS = "configurations/test1.properties";


  @Test(expected = InvalidConfigurationEntityException.class)
  public void testIsRegisteredTestUsingInvalidPropsEntity() {
    final ConfigurationManager configurationManager = ConfigurationManager
            .newManager(Optional.empty());
    configurationManager.register(Object.class);
  }


  @Test
  public void testIsRegisteredTestUsingValidPropsEntityButAlreadyRegistered() {
    final ConfigurationManager configurationManager = ConfigurationManager
            .newManager(Optional.empty());
    assertThat(configurationManager.register(Test1ConfigEntity.class), equalTo(true));
    assertThat(configurationManager.register(Test1ConfigEntity.class), equalTo(false));
  }


  @Test
  public void testIsRegisteredTestUsingValidPropsEntity() {
    final ConfigurationManager configurationManager = ConfigurationManager
            .newManager(Optional.empty());
    configurationManager.register(Test1ConfigEntity.class);
    assertThat(configurationManager.isRegistered(Test1ConfigEntity.class), equalTo(true));
  }


  @Test
  public void testLoadFromClasspath() throws InterruptedException {
    final ConfigurationManager configurationManager = ConfigurationManager
            .newManager(Optional.empty());
    configurationManager.register(Test1ConfigEntity.class);

    Thread.sleep(100L);
    final Optional<Test1ConfigEntity> configs = configurationManager.load(Test1ConfigEntity.class);
    assertThat(configs.isPresent(), equalTo(true));

    final Test1ConfigEntity testPropsWithClasspathSource = configs.get();
    assertThat(testPropsWithClasspathSource.getUrl(), equalTo("http://localhost/"));
    assertThat(testPropsWithClasspathSource.getTimeout(), equalTo(1000L));
    assertThat(testPropsWithClasspathSource.getCount(), equalTo(99));
  }


  @Test
  public void testLoadFromClasspathWithMultipleContext() throws
          InterruptedException {
    final ContextInfo contextInfo = ContextInfo.of("eu", "dev");

    final ConfigurationManager configurationManager = ConfigurationManager
            .newManager(Optional.of(contextInfo));
    configurationManager.register(Test1ConfigEntity.class);

    Thread.sleep(100L);
    final Optional<Test1ConfigEntity> configs = configurationManager.load(Test1ConfigEntity.class, Optional.of(contextInfo));
    assertThat(configs.isPresent(), equalTo(true));

    final Test1ConfigEntity testPropsWithClasspathSource = configs.get();
    assertThat(testPropsWithClasspathSource.getUrl(), equalTo("http://localhost/"));
    assertThat(testPropsWithClasspathSource.getTimeout(), equalTo(1002L));
    assertThat(testPropsWithClasspathSource.getCount(), equalTo(99));
  }


  @Test
  public void testLoadFromClasspathWithSingleContext() throws
          InterruptedException {
    final ContextInfo contextInfo = ContextInfo.of("eu");

    final ConfigurationManager configurationManager = ConfigurationManager
            .newManager(Optional.of(contextInfo));
    configurationManager.register(Test1ConfigEntity.class);

    Thread.sleep(100L);
    final Optional<Test1ConfigEntity> configs = configurationManager.load(Test1ConfigEntity.class, Optional.of(contextInfo));
    assertThat(configs.isPresent(), equalTo(true));

    final Test1ConfigEntity testPropsWithClasspathSource = configs.get();
    assertThat(testPropsWithClasspathSource.getUrl(), equalTo("http://localhost/"));
    assertThat(testPropsWithClasspathSource.getTimeout(), equalTo(1001L));
    assertThat(testPropsWithClasspathSource.getCount(), equalTo(99));
  }


  @Test
  public void testLoadFromClasspathOnlyGlobalConfigurations() throws
          InterruptedException {
    final ContextInfo contextInfo = ContextInfo.of("eu");

    final ConfigurationManager configurationManager = ConfigurationManager
            .newManager(Optional.of(contextInfo));
    configurationManager.register(Test1ConfigEntity.class);

    Thread.sleep(100L);
    final Optional<Test1ConfigEntity> configs = configurationManager.load(Test1ConfigEntity.class, Optional.of(contextInfo));
    assertThat(configs.isPresent(), equalTo(true));

    final Test1ConfigEntity testPropsWithClasspathSource = configs.get();
    assertThat(testPropsWithClasspathSource.getUrl(), equalTo("http://localhost/"));
    assertThat(testPropsWithClasspathSource.getTimeout(), equalTo(1001L));
    assertThat(testPropsWithClasspathSource.getCount(), equalTo(99));
  }
}
