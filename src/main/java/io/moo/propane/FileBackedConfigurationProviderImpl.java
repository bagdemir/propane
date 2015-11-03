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

import io.moo.propane.annotation.Source;
import io.moo.propane.annotation.processor.AnnotationProcessor;
import io.moo.propane.annotation.processor.PropsAnnotationProcessorImpl;
import io.moo.propane.connectors.ClasspathConfigurationSource;
import io.moo.propane.connectors.ConfigData;
import io.moo.propane.connectors.PropertiesFileConfigurationSource;
import io.moo.propane.data.ConfigurationEntity;
import io.moo.propane.exception.InvalidPropsEntityException;
import io.moo.propane.extractors.DefaultComponentIdExtractor;
import io.moo.propane.extractors.TokenExtractor;

/**
 * Properties provider.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class FileBackedConfigurationProviderImpl<T> implements ConfigurationProvider<T> {
  private static final String CLASSPATH_PREFIX = "classpath://";
  public static final String FILE_PREFIX = "file://";
  public static final String BLANK_STR = "";

  private final Class<T> propsClazz;
  private io.moo.propane.connectors.ConfigurationSource connector;
  private TokenExtractor contextExtractor;
  private TokenExtractor componentIdExtractor;


  public FileBackedConfigurationProviderImpl(final Class<T> propsClazz) {
    this.propsClazz = propsClazz;
    this.componentIdExtractor = new DefaultComponentIdExtractor();

    init();
  }


  private void init() {
    final Source source = propsClazz.getAnnotation(Source.class);

    if (source == null) throw new InvalidPropsEntityException();

    String url = source.url();
    if (isClasspathResource(url)) {
      final String replace = url.replace(CLASSPATH_PREFIX, BLANK_STR);
      connector = new ClasspathConfigurationSource(replace);
    } else {
      final String replace = url.replace(FILE_PREFIX, BLANK_STR);
      connector = new PropertiesFileConfigurationSource(replace);
    }
  }


  private boolean isClasspathResource(final String url) {
    return url.startsWith(CLASSPATH_PREFIX);
  }


  @Override
  public T take() {
    final ConfigData configData = connector.read();
    Map<String, String> propsMap = connector.read().getPropsMap();
    List<ConfigurationEntity> propsList = propsMap.entrySet().stream().map(entry ->
            new ConfigurationEntity(componentIdExtractor.extract(configData.getSource()),
                    null, entry.getKey(), entry.getValue())).collect(Collectors.toList());
    AnnotationProcessor processor = new PropsAnnotationProcessorImpl();
    return processor.createEntity(propsClazz, propsList);
  }
}
