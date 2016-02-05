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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.moo.propane.annotation.Configuration;
import io.moo.propane.annotation.KeyValue;
import io.moo.propane.annotation.Source;
import io.moo.propane.data.ConfigurationEntity;
import io.moo.propane.data.ContextInfo;
import io.moo.propane.exception.InvalidConfigurationEntityException;
import io.moo.propane.extractors.TokenExtractor;
import io.moo.propane.sources.ConfigData;

/**
 * Process the configuration annotations provided in configuration entities.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class ConfigurationAnnotationProcessorImpl implements AnnotationProcessor {
  private static final Logger LOG = LogManager.getLogger();


  private boolean isWithinContext(final Optional<ContextInfo> contextInfo, final ConfigurationEntity entity) {
    if (!contextInfo.isPresent() || contextInfo.get().getContexts().isEmpty()) {
      return true;
    }
    return entity.getContextIds().containsAll(contextInfo.get().getContexts());
  }


  @Override
  public <T> T createEntity(final Class<T> clazz, final ConfigData data) {
    return createEntity(clazz, data, Optional.empty());
  }


  public <T> T createEntity(final Class<T> clazz, final ConfigData configData, final Optional<ContextInfo> contextInfo) {
    final Source source = clazz.getDeclaredAnnotation(Source.class);
    final TokenExtractor idExtractor = getComponentIdExtractorInstance(source);
    final TokenExtractor contextExtractor = getComponentContextExtractorInstance(source);
    final List<ConfigurationEntity> entities = configData.getPropsMap().entrySet().stream().
            map(entity -> AnnotationProcessor.newEntity(idExtractor, contextExtractor, configData, entity)).
            collect(Collectors.toList());

    final Configuration configurationAnnotation = clazz.getDeclaredAnnotation(Configuration.class);
    if (configurationAnnotation != null) {
      final T instance = newEntityInstance(clazz);
      filterEntities(entities, contextInfo);
      processFields(entities, configurationAnnotation.componentId(), clazz.getDeclaredFields(), instance);
      return instance;
    } else
      throw new InvalidConfigurationEntityException("@Configuration annotation is missing.");
  }


  private List<ConfigurationEntity> filterEntities(final List<ConfigurationEntity> entities, final Optional<ContextInfo> contextInfo) {
    Collections.sort(entities, (first, second) -> first.getContextIds().size() - second.getContextIds().size());
    final List<ConfigurationEntity> result = new ArrayList<>();

    final Iterator<ConfigurationEntity> iterator = entities.iterator();
    int current = 0;
    while (iterator.hasNext()) {

      final ConfigurationEntity next = iterator.next();
      final Collection<String> contextIds = next.getContextIds();
      final ListIterator<ConfigurationEntity> configurationEntityListIterator = entities.listIterator(current);
      while (configurationEntityListIterator.hasNext()) {
        final ConfigurationEntity entity = configurationEntityListIterator.next();
        if (AnnotationProcessor.getPropertyNameFromFQPropertyName(next.getPropertyName()).equals(AnnotationProcessor.getPropertyNameFromFQPropertyName(entity.getPropertyName())) &&
                entity.getContextIds().size() > contextIds.size() &&
                contextInfo.get().getContexts().containsAll(entity.getContextIds())) {

        } else {
          if (!result.contains(next)) result.add(next);
        }
      }
      current++;
    }

    return result;
  }


  private TokenExtractor getComponentIdExtractorInstance(final Source source) {

    try {
      if (source == null) {
        throw new InvalidConfigurationEntityException("@Source annotation is missing.");
      } else {
        return source.componentIdExtractor().newInstance();
      }
    }
    catch (InstantiationException | IllegalAccessException e) {
      LOG.error(e);
      throw new InvalidConfigurationEntityException(e.getMessage(), e);
    }
  }


  private TokenExtractor getComponentContextExtractorInstance(final Source source) {

    try {
      if (source == null) {
        throw new InvalidConfigurationEntityException("@Source annotation is missing.");
      } else {
        return source.contextExtractor().newInstance();
      }
    }
    catch (InstantiationException | IllegalAccessException e) {
      LOG.error(e);
      throw new InvalidConfigurationEntityException(e.getMessage(), e);
    }
  }


  private <T> void processFields(final Collection<ConfigurationEntity> entities,
          final String componentId, final Field[] fields, final T instance) {

    Arrays.stream(fields).
            filter(field -> null != field.getAnnotation(KeyValue.class)).
            forEach(field -> entities.stream().
                    filter(entity -> entity.getComponentId().equals(componentId)).
                    forEach(entity -> performEntityProcessing(field.getAnnotation(KeyValue.class),
                            entity, field, instance)));
  }


  private <T> void performEntityProcessing(final KeyValue annotation, final ConfigurationEntity configurationEntity,
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
