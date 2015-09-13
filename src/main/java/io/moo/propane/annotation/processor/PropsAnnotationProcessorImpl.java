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
package io.moo.propane.annotation.processor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.moo.propane.annotation.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.moo.propane.annotation.KeyValue;
import io.moo.propane.exception.InvalidPropsEntityException;

/**
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class PropsAnnotationProcessorImpl implements AnnotationProcessor {
  private static final Logger LOG = LogManager.getLogger();


  @Override
  public <T> T createEntity(final Class<T> clazz, final List<io.moo.propane.data.ConfigurationEntity> entities) {
    Configuration configurationAnnotation = clazz.getDeclaredAnnotation(Configuration.class);
    if (configurationAnnotation != null) {
      String componentId = configurationAnnotation.componentId();
      Field[] fields = clazz.getDeclaredFields();
      T instance = newEntityInstance(clazz);
      processFields(entities, componentId, fields, instance);
      return instance;
    } else throw new InvalidPropsEntityException();
  }


  private <T> void processFields(final Collection<io.moo.propane.data.ConfigurationEntity> entities,
          final String componentId,
          final Field[] fields,
          final T instance) {
    Arrays.stream(fields).
            filter(field -> null != field.getAnnotation(KeyValue.class)).
            forEach(field -> entities.stream().
                    filter(entity -> entity.getComponentId().equals(componentId)).
                    forEach(entity -> performEntityProcessing(field.getAnnotation(KeyValue.class),
                            entity, field, instance)));
  }


  private <T> void performEntityProcessing(final KeyValue annotation, final io.moo.propane.data.ConfigurationEntity configurationEntity,
          final Field field, T instance) {
    if (annotation.name().equals(configurationEntity.getPropertyName())) {
      Object propertyValue = configurationEntity.getPropertyValue();
      if (isWrapper(field.getType()) || field.getType().isPrimitive()) {
        performPrimitiveAssignment(instance, field, propertyValue);
      } else if (field.getType().isAssignableFrom(propertyValue.getClass())) {
        performAssignment(instance, field, propertyValue);
      }
    }
  }


  private boolean isWrapper(final Class<?> type) {
    return type.equals(Integer.class) || type.equals(Long.class) ||
            type.equals(Double.class) ||
            type.equals(Float.class) ||
            type.equals(Short.class) ||
            type.equals(Byte.class) ||
            type.equals(Boolean.class) ||
            type.equals(Character.class);
  }


  private <T> void performPrimitiveAssignment(final T instance,
          final Field field,
          final Object propertyValue) {
    final Class<?> fieldType = field.getType();
    if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
      performAssignment(instance, field, Integer.valueOf((String) propertyValue));
    } else if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
      performAssignment(instance, field, Long.valueOf((String) propertyValue));
    } else if (fieldType.equals(double.class) || fieldType.equals(Double.class)) {
      performAssignment(instance, field, Double.valueOf((String) propertyValue));
    } else if (fieldType.equals(float.class) || fieldType.equals(Float.class)) {
      performAssignment(instance, field, Float.valueOf((String) propertyValue));
    } else if (fieldType.equals(short.class) || fieldType.equals(Short.class)) {
      performAssignment(instance, field, Short.valueOf((String) propertyValue));
    } else if (fieldType.equals(byte.class) || fieldType.equals(Byte.class)) {
      performAssignment(instance, field, Byte.valueOf((String) propertyValue));
    } else if (fieldType.equals(char.class) || fieldType.equals(Character.class)) {
      performAssignment(instance, field, Byte.valueOf((String) propertyValue));
    } else if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
      performAssignment(instance, field, Boolean.valueOf((String) propertyValue));
    }
  }


  private <T> void performAssignment(final T instance,
          final Field field,
          final Object propertyValue) {
    try {
      field.setAccessible(true);
      field.set(instance, propertyValue);
    }
    catch (IllegalAccessException e) {
      LOG.error(e);
    } finally {
      field.setAccessible(false);
    }
  }


  private <T> T newEntityInstance(final Class<T> clazz) {
    try {
      return clazz.newInstance();
    }
    catch (InstantiationException | IllegalAccessException e) {
      LOG.error(e);
    }
    throw new AssertionError("Cannot createEntity a new instance of :" + clazz.getName());
  }
}
