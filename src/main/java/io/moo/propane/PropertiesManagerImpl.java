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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.moo.propane.annotation.PropsEntity;
import io.moo.propane.annotation.processor.AnnotationProcessor;
import io.moo.propane.annotation.processor.PropsAnnotationProcessorImpl;
import io.moo.propane.exception.InvalidPropsEntityException;

/**
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class PropertiesManagerImpl implements PropertiesManager {
  private static final Logger LOG = LogManager.getLogger();
  private final Map<PropertiesProvider, Object> objectCache = new ConcurrentHashMap<>();
  private final AnnotationProcessor processor = new PropsAnnotationProcessorImpl();


  @Override
  public <T> boolean register(final Class<T> clazz) {
    PropertiesProvider<T> propertiesProvider = new PropertiesProviderImpl<>(clazz);
    if (objectCache.containsKey(propertiesProvider)) {
      LOG.info("{} has already been registered.", clazz);
      return false;
    } else {
      validatePropsEntity(clazz);
      registerPropsProvider(propertiesProvider);
    }
    return true;
  }


  private void registerPropsProvider(final PropertiesProvider propertiesProvider) {
    objectCache.put(propertiesProvider.init(), null);
  }


  @Override
  public <T> boolean isRegistered(final Class<T> clazz) {
    return objectCache.containsKey(clazz);
  }


  private <T> void validatePropsEntity(final Class<T> clazz) {
    final PropsEntity propsEntityAnnotation = clazz.getAnnotation(PropsEntity.class);
    if (propsEntityAnnotation == null) {
      throw new InvalidPropsEntityException();
    }
  }


  @Override
  public <T> Optional<T> load(final Class<T> clazz) {
    return null;
  }


  @Override
  public <T> Optional<T> load(final String componentId) {
    return null;
  }
}
