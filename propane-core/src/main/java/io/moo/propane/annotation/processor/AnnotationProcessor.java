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
package io.moo.propane.annotation.processor;

import io.moo.propane.data.ConfigurationEntity;
import io.moo.propane.data.ContextInfo;
import io.moo.propane.extractors.TokenExtractor;
import io.moo.propane.sources.ConfigData;

import java.util.Map;
import java.util.Optional;

/**
 * Annotation processor is used to process configuration entity annotations.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public interface AnnotationProcessor {

  String BLANK_STRING = "";

  <T> T createEntity(final Class<T> clazz, final ConfigData data);

  <T> T createEntity(final Class<T> clazz, final ConfigData data, final Optional<ContextInfo> contextInfo);

  /**
   * Static factory method that creates configuration entity instances from the
   * raw configuration data.
   *
   * @param componentIdExtractor Component id extractor.
   * @param contextExtractor     Context extractor.
   * @param source               Configuration source.
   * @param entry                Configuration entry as key, value.
   * @return Configuration entity.
   * @see ConfigurationEntity
   * @see ConfigData
   */
  static ConfigurationEntity newEntity(final TokenExtractor componentIdExtractor,
          final TokenExtractor contextExtractor,
          final String source,
          final Map.Entry<String, String> entry) {

    return new ConfigurationEntity(String.join(BLANK_STRING, componentIdExtractor.extract(source)),
            contextExtractor.extract(entry.getKey()), getPropertyNameFromFQPropertyName(entry.getKey()),
            entry.getValue());
  }

  /**
   * Returns the plain property name from a fully-qualified property name.
   *
   * @param FQPropertyName Fully-qualified property name.
   * @return Plain property name.
   */
  static String getPropertyNameFromFQPropertyName(final String FQPropertyName) {
    if (FQPropertyName.contains(".")) {
      final String[] split = FQPropertyName.split("\\.");
      return split[split.length - 1];
    }
    return FQPropertyName;
  }
}
