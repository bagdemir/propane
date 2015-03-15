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
package io.moo.propane.annotation;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.moo.propane.data.PropertiesEntity;

/**
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class PropsEntityProcessorImpl implements AnnotationProcessor {
  private static final Logger LOG = LogManager.getLogger();


  @Override
  public <T> T create(final Class<T> clazz, final Collection<PropertiesEntity> entities) {
    checkNotNull(entities, "entities == null");
    checkArgument(!entities.isEmpty(), "!entities.isEmpty()");

    final PropsEntity declaredAnnotation = clazz.getDeclaredAnnotation(PropsEntity.class);
    if (declaredAnnotation == null) {
      throw new InvalidPropsEntityException();
    }
    final String componentId = declaredAnnotation.componentId();
    final Field[] fields = clazz.getDeclaredFields();
    final T instance = newEntityInstance(clazz);
    for (final Field field : fields) {
      final Prop annotation = field.getAnnotation(Prop.class);
      if (annotation != null) {
        entities.stream().
          filter(propertiesEntity -> propertiesEntity.getComponentId().equals(componentId)).
          forEach(propertiesEntity -> {
            if (annotation.name().equals(propertiesEntity.getPropertyName())) {
              final Object propertyValue = propertiesEntity.getPropertyValue();
              if (field.getType().isAssignableFrom(propertyValue.getClass())) {
                try {
                  field.setAccessible(true);
                  field.set(instance, propertyValue);
                  field.setAccessible(false);
                }
                catch (IllegalAccessException e) {
                  LOG.error(e);
                }
              }
            }
          });
      }
    }

    return instance;
  }


  private <T> T newEntityInstance(final Class<T> clazz) {
    T instance = null;
    try {
      instance = clazz.newInstance();
    }
    catch (InstantiationException e) {
      LOG.error(e);
    }
    catch (IllegalAccessException e) {
      LOG.error(e);
    }
    return instance;
  }
}
