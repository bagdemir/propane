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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.moo.propane.annotation.PropsSource;
import io.moo.propane.annotation.processor.AnnotationProcessor;
import io.moo.propane.annotation.processor.PropsAnnotationProcessorImpl;
import io.moo.propane.connectors.ClasspathPropertiesResourceConnector;
import io.moo.propane.connectors.PropertiesResourceConnector;
import io.moo.propane.data.PropertiesEntity;
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
    this.componentIdExtractor = new DefaultComponentIdExtractor();
    init();
  }


  private void init() {
    final PropsSource propsSource = propsClazz.getAnnotation(PropsSource.class);
    if (propsSource == null) {
      throw new InvalidPropsEntityException();
    }
    final String url = propsSource.url();
    if (url.startsWith(CLASSPATH_PREFIX)) {
      connector = new ClasspathPropertiesResourceConnector(url.replace(CLASSPATH_PREFIX, ""));
    }
  }


  @Override
  public T take() {
    Map<String, String> propsMap = connector.read();
    List<PropertiesEntity> propsList = propsMap.entrySet().stream().map(entry ->
      new PropertiesEntity(componentIdExtractor.extract(entry.getKey()),
        null, entry.getKey().replace(componentIdExtractor.extract(entry.getKey()).concat("/"), ""), entry.getValue())).collect(Collectors.toList());
    AnnotationProcessor processor = new PropsAnnotationProcessorImpl();
    return processor.createEntity(propsClazz, propsList);
  }
}
