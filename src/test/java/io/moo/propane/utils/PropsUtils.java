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
package io.moo.propane.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Some utility methods used by tests.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class PropsUtils {

  public static void createTestPropertiesFile(String configDir, String fileName) throws IOException {
    FileWriter fileWriter = null;
    try {
      final Properties props = new Properties();
      props.setProperty("propertyName", "propertyValue");
      final File cfgFile = new File(configDir, fileName);
      fileWriter = new FileWriter(cfgFile);
      props.store(fileWriter, "test config");
    } finally {
      if (fileWriter != null) {
        fileWriter.flush();
        fileWriter.close();
      }
    }
  }

  public static String getPathToTemp() throws IOException {
    final File tmpCfg = File.createTempFile("test-properties", ".properties");
    final String absolutePathOfCfgFile1 = tmpCfg.getAbsolutePath();
    final int lastIndexOfSlash = absolutePathOfCfgFile1.lastIndexOf("/");
    return absolutePathOfCfgFile1.substring(0, lastIndexOfSlash);
  }
}
