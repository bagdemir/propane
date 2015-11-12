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

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.moo.propane.data.ConfigurationEntity;
import io.moo.propane.utils.PropsUtils;

/**
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class DirectoryPropertiesProviderTest {

  private static final String CFG_FILE_1 = "testConfig.properties";
  private static final String CFG_FILE_2 = "test2.cfg";
  private static final String NON_CFG_FILE_3 = "test3.txt";
  private static final Logger LOG = LogManager.getLogger();
  private File configDir = null;


  @Before
  public void setUp() throws IOException {

    configDir = new File(PropsUtils.getPathToTemp(), "/configs");

    LOG.info("Creating temporary configuration files: %s", configDir);

    verifyConfigDirCreated();

    PropsUtils.createTestPropertiesFile(configDir.getAbsolutePath(), CFG_FILE_1);
    PropsUtils.createTestPropertiesFile(configDir.getAbsolutePath(), CFG_FILE_2);
    PropsUtils.createTestPropertiesFile(configDir.getAbsolutePath(), NON_CFG_FILE_3);
  }


  private void verifyConfigDirCreated() {
    if (!configDir.mkdir()) {
      throw new RuntimeException("The config dir can not be created.");
    }
  }


  @Test
  public void testRead() {
    final DirectoryConfigurationProviderImpl configProvider = new DirectoryConfigurationProviderImpl();
    final Collection<ConfigurationEntity> read = configProvider.read(configDir.getAbsolutePath());
  }


  @After
  public void tearDown() throws IOException {
    if (configDir != null) {
      FileUtils.deleteDirectory(configDir);
      LOG.info("Cleaned up the resources: %s", configDir);
    }
  }
}
