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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.moo.propane.annotation.Prop;
import io.moo.propane.annotation.PropsEntity;
import io.moo.propane.data.PropertiesEntity;
import io.moo.propane.exception.InvalidPropsEntityException;

/**
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class PropsEntityAnnotationProcessorImpl implements AnnotationProcessor {
  private static final Logger LOG = LogManager.getLogger();


  @Override
  public <T> T create(final Class<T> clazz, final Collection<PropertiesEntity> entities) {
    final PropsEntity propsEntityAnnotation = clazz.getDeclaredAnnotation(PropsEntity.class);
    if (propsEntityAnnotation != null) {
      String componentId = propsEntityAnnotation.componentId();
      Field[] fields = clazz.getDeclaredFields();
      T instance = newEntityInstance(clazz);
      processFields(entities, componentId, fields, instance);
      return instance;
    } else throw new InvalidPropsEntityException();
  }


  private <T> void processFields(final Collection<PropertiesEntity> entities,
    final String componentId,
    final Field[] fields,
    final T instance) {
    Arrays.stream(fields).
      filter(field -> null != field.getAnnotation(Prop.class)).
      forEach(field -> entities.stream().
        filter(entity -> entity.getComponentId().equals(componentId)).
        forEach(entity -> performEntityProcessing(field.getAnnotation(Prop.class),
          entity, field, instance)));
  }

  private <T> void performEntityProcessing(final Prop annotation, final PropertiesEntity propertiesEntity, final Field field, T instance){
    if (annotation.name().equals(propertiesEntity.getPropertyName())) {
      Object propertyValue = propertiesEntity.getPropertyValue();
      if (field.getType().isAssignableFrom(propertyValue.getClass())) {
        performAssignment(instance, field, propertyValue);
      }
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
    throw new AssertionError("Cannot create a new instance of :" + clazz.getName());
  }
}
