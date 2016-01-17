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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import io.moo.propane.annotation.TestConfigurationEntityWithClasspathSource;
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
  private static final String TEST_PROPS = "configurations/test.properties";
  private static File testConfigFile;


  @BeforeClass
  public static void setUp() throws IOException {

    testConfigFile = File.createTempFile(String.format("test-%s", UUID.randomUUID()), ".tmp");

    LOG.info("Created a new file file %s", testConfigFile);

    final FileOutputStream fileOutputStream = new FileOutputStream(testConfigFile);
    final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
    final Properties props = new Properties();
    props.put("longProp", "1000");
    props.put("testProp", "http://localhost/");
    props.put("intProp", "99");

    try {
      props.store(outputStreamWriter, "Test properties used by tests.");
    }
    catch (IOException e) {
      try {
        outputStreamWriter.close();
      }
      catch (IOException ioe) {
        throw new RuntimeException(String.format("Cannot close the stream for %s file.", TEST_PROPS));
      }
    }
  }


  @AfterClass
  public static void tearDown() {
    if (testConfigFile != null && testConfigFile.exists() && testConfigFile.delete()) {
      LOG.info("Test file %s successfully deleted.", testConfigFile);
    }
  }


  @Test(expected = InvalidConfigurationEntityException.class)
  public void testIsRegisteredTestUsingInvalidPropsEntity() {
    final ConfigurationManager configurationManager = new ConfigurationManagerImpl();
    configurationManager.register(Object.class);
  }


  @Test
  public void testIsRegisteredTestUsingValidPropsEntityButAlreadyRegistered() {
    final ConfigurationManager configurationManager = new ConfigurationManagerImpl();
    assertThat(configurationManager.register(TestConfigurationEntityWithClasspathSource.class), equalTo(true));
    assertThat(configurationManager.register(TestConfigurationEntityWithClasspathSource.class), equalTo(false));
  }


  @Test
  public void testIsRegisteredTestUsingValidPropsEntity() {
    final ConfigurationManager configurationManager = new ConfigurationManagerImpl();
    configurationManager.register(TestConfigurationEntityWithClasspathSource.class);
    assertThat(configurationManager.isRegistered(TestConfigurationEntityWithClasspathSource.class), equalTo(true));
  }


  @Test
  public void testLoad() throws InterruptedException {
    final ConfigurationManager configurationManager = new ConfigurationManagerImpl();
    configurationManager.register(TestConfigurationEntityWithClasspathSource.class);

    Thread.sleep(100L);
    final Optional<TestConfigurationEntityWithClasspathSource> configs = configurationManager.load(TestConfigurationEntityWithClasspathSource.class);
    assertThat(configs.isPresent(), equalTo(true));

    final TestConfigurationEntityWithClasspathSource testPropsWithClasspathSource = configs.get();
    assertThat(testPropsWithClasspathSource.getUrl(), equalTo("http://localhost/"));
    assertThat(testPropsWithClasspathSource.getTimeout(), equalTo(1000L));
    assertThat(testPropsWithClasspathSource.getCount(), equalTo(99));
  }
}
