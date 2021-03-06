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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import io.moo.propane.exception.NoComponentIdFoundException;

/**
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class DefaultComponentIdExtractor implements TokenExtractor {

  @Override
  public Set<String> extract(final String sourceString) {
    assertPropertyNameIsValid(sourceString);
    int slLastIndex = sourceString.lastIndexOf(SEGMENT_SEPARATOR);
    int ptLastIndex = sourceString.lastIndexOf(".");
    if (slLastIndex < ptLastIndex) {
      return Collections.singleton(sourceString.substring(slLastIndex + 1, ptLastIndex));
    } else if (slLastIndex > -1) {
      return Collections.singleton(sourceString.substring(slLastIndex + 1));
    } else throw new NoComponentIdFoundException(sourceString);
  }


  private void assertPropertyNameIsValid(final String source) {
    if (source == null) throw new NoComponentIdFoundException(source);
  }
}
