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
package io.moo.propane.sources;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * {@link ClasspathConfigurationSource} uses classpath resources as
 * sources.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class ClasspathConfigurationSource extends ConfigurationSource {
  private static final Logger LOG = LogManager.getLogger();


  public ClasspathConfigurationSource(final String source) {
    super(source);
  }


  @Override
  public ConfigData read() {
    ConfigData configData = new ConfigData();
    Map<String, String> propsMap = configData.getPropsMap();

    try {
      Properties properties = new Properties();
      properties.load(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(source)));
      properties.forEach((name,value) -> propsMap.put(name.toString(), value.toString()));
    }
    catch (IOException e) {
      LOG.error(e);
    }

    configData.setSource(super.getSource());
    return configData;
  }
}
