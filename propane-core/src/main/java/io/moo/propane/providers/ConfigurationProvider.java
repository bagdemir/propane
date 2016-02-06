/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 moo.io , Erhan Bagdemir
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.moo.propane.providers;

import java.util.Optional;

import io.moo.propane.data.ContextInfo;
import io.moo.propane.sources.ClasspathFileConfigurationSource;
import io.moo.propane.sources.ConfigurationSource;
import io.moo.propane.sources.PropertiesFileConfigurationSource;

/**
 * Configuration provider.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public interface ConfigurationProvider<T> {
  static final String CLASSPATH_PREFIX = "classpath://";
  static final String FILE_PREFIX = "file://";
  static final String DB_MYSQL_PREFIX = "mysql://";
  static final String BLANK_STR = "";

  static <E> ConfigurationProvider<E> create(
          Class<E> clazz, String source, int refreshInSeconds) {

    final String[] split = source.split("://");
    if (split.length == 0) {
      throw new RuntimeException("Source URL doesn't seem valid.");
    }

    switch (split[0]) {
      case "classpath":
      case "file":
        return new FileBackedConfigurationProviderImpl<>(clazz, ConfigurationSource.of(source), refreshInSeconds);
      default:
        throw new RuntimeException("Unknown source type.");
    }

  }

  static <E> ConfigurationProvider<E> createFileBackedProvider(
          Class<E> clazz, String source, int refreshInSeconds) {

    final ConfigurationSource configSource = ConfigurationSource.of(source);

    return new FileBackedConfigurationProviderImpl<>(clazz, configSource, refreshInSeconds);
  }

  T load(Class<T> clazz);

  T load(Class<T> clazz, Optional<ContextInfo> contextInfo);
}
