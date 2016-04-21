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
package io.moo.propane.providers.impl;

import io.moo.propane.annotation.processor.ConfigurationAnnotationProcessorImpl;
import io.moo.propane.data.ContextInfo;
import io.moo.propane.extractors.DefaultComponentIdExtractor;
import io.moo.propane.extractors.TokenExtractor;
import io.moo.propane.providers.ScheduledConfigurationProvider;
import io.moo.propane.sources.ConfigurationSource;

import java.util.Optional;

/**
 * File backed configuration provider.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class FileBackedConfigurationProviderImpl<T> extends ScheduledConfigurationProvider<T> {

  private final TokenExtractor componentIdExtractor;

  public FileBackedConfigurationProviderImpl(final Class<T> propsClazz, final ConfigurationSource source, final int refreshFrequencyInSeconds) {
    super(propsClazz, source, refreshFrequencyInSeconds);

    this.componentIdExtractor = new DefaultComponentIdExtractor();
  }

  @Override
  public T load(final Class<T> clazz) {
    return load(clazz, Optional.empty());
  }

  @Override
  public T load(final Class<T> clazz, final Optional<ContextInfo> contextInfo) {
    return new ConfigurationAnnotationProcessorImpl().createEntity(clazz,
            configData.get().get(), contextInfo);
  }
}
