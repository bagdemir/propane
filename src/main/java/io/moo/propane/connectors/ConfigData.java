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
package io.moo.propane.connectors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class ConfigData {

  private Map<String, String> propsMap = new HashMap<>();
  private String source;


  public Map<String, String> getPropsMap() {
    return propsMap;
  }


  public void setPropsMap(final Map<String, String> propsMap) {
    this.propsMap = propsMap;
  }


  public String getSource() {
    return source;
  }


  public void setSource(final String source) {
    this.source = source;
  }


  @Override
  public String toString() {
    return "ConfigData{" +
            "propsMap=" + propsMap +
            ", source='" + source + '\'' +
            '}';
  }
}
