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
package io.moo.propane;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.moo.propane.annotation.Configuration;
import io.moo.propane.annotation.Source;
import io.moo.propane.exception.InvalidConfigurationEntityException;
import io.moo.propane.providers.ConfigurationProvider;
import io.moo.propane.providers.FileBackedConfigurationProviderImpl;
import io.moo.propane.sources.ConfigurationSource;

/**
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class ConfigurationManagerImpl implements ConfigurationManager {
  private static final Logger LOG = LogManager.getLogger();

  private final Map<Class<?>, ConfigurationProvider> cache = new ConcurrentHashMap<>();


  @Override
  public <T> boolean register(final Class<T> clazz) {
    if (cache.containsKey(clazz)) {
      LOG.info("{} has already been registered.", clazz);
      return false;
    }
    validateConfigurationEntity(clazz);
    registerConfigurationProvider(clazz);
    return true;
  }


  private void registerConfigurationProvider(final Class<?> clazz) {

    final Source source = clazz.getAnnotation(Source.class);
    final ConfigurationSource configSource = ConfigurationSource.newConfigurationSourceFor(source);

    cache.put(clazz, new FileBackedConfigurationProviderImpl<>(clazz, configSource, 60 /*TODO make configurable*/));
  }


  @Override
  public <T> boolean isRegistered(final Class<T> clazz) {
    return cache.containsKey(clazz);
  }


  private <T> void validateConfigurationEntity(final Class<T> clazz) {
    final Configuration configurationAnnotation = clazz.getAnnotation(Configuration.class);
    if (configurationAnnotation == null) {
      throw new InvalidConfigurationEntityException();
    }
  }


  @Override
  public <T> Optional<T> load(final Class<T> clazz) {
    if (isRegistered(clazz)) {
      final ConfigurationProvider provider = cache.get(clazz);
      return Optional.ofNullable((T) provider.load(clazz));
    }
    return Optional.empty();
  }
}
