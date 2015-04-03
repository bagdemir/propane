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

import java.util.Map;

import io.moo.propane.annotation.PropsSource;
import io.moo.propane.connectors.ClasspathPropertiesResourceConnector;
import io.moo.propane.connectors.PropertiesResourceConnector;
import io.moo.propane.exception.InvalidPropsEntityException;

/**
 * Properties provider.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class PropertiesProviderImpl<T> implements PropertiesProvider<T> {
  private static final String CLASSPATH_PREFIX = "classpath://";
  private final Class<T> propsClazz;
  private PropertiesResourceConnector connector;
  private TokenExtractor contextExtractor;
  private TokenExtractor componentIdExtractor;

  public PropertiesProviderImpl(final Class<T> propsClazz) {
    this.propsClazz = propsClazz;
  }

  public PropertiesProvider init() {
    final PropsSource propsSource = propsClazz.getAnnotation(PropsSource.class);
    if (propsSource == null) {
      throw new InvalidPropsEntityException();
    }
    final String url = propsSource.url();
    if (url.startsWith(CLASSPATH_PREFIX)) {
      connector = new ClasspathPropertiesResourceConnector(url);
    }
    return this;
  }


  @Override
  public T take() {
    final Map<String, Object> map = connector.read();
    // map.forEach((key, value) -> key.split());

    return null;
  }


  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof PropertiesProviderImpl)) return false;
    PropertiesProviderImpl that = (PropertiesProviderImpl) o;
    return propsClazz.equals(that.propsClazz);
  }


  @Override
  public int hashCode() {
    return propsClazz.hashCode();
  }
}
