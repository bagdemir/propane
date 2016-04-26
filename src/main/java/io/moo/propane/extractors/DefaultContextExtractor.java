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
package io.moo.propane.extractors;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import io.moo.propane.exception.InvalidPropertyNameException;

/**
 * Context extractor to determine the context of a configuration entity.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class DefaultContextExtractor implements TokenExtractor {

  @Override
  public Collection<String> extract(final String sourceString) {
    assertPropertyNameIsValid(sourceString);

    if (sourceString.contains(SEGMENT_SEPARATOR)) {
      String[] split = sourceString.split(SEGMENT_SEPARATOR);
      if (split.length > 2) {
        return Arrays.asList(split[split.length - 3].split(CONTEXT_SEPARATOR));
      }
    }
    return Collections.<String>emptyList();
  }

  private void assertPropertyNameIsValid(final String propertyName) {
    if (propertyName == null || BLANK_STR.equals(propertyName)) {
      throw new InvalidPropertyNameException();
    }
  }
}
