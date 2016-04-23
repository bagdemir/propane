/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 moo.io , Erhan Bagdemir
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

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.moo.propane.annotation.KeyValue;
import io.moo.propane.annotation.Source;
import io.moo.propane.exception.InvalidConfigurationEntityException;

/**
 * Utility methods for annotation processing.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class AnnotationUtils {

  public static String getSourceURL(final Class<?> clazz) {
    return Optional.ofNullable(clazz.getAnnotation(Source.class)).orElseThrow(() -> new
            InvalidConfigurationEntityException("@Source annotation is missing in the configuration entity.")).url();
  }

  public static List<String> getDefinedProperties(final Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields())
            .map((f) -> Optional.ofNullable(f.getAnnotation(KeyValue.class)))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(KeyValue::name)
            .collect(Collectors.toList());
  }
}
