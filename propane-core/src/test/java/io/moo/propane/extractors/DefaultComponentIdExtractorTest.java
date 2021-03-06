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
package io.moo.propane.extractors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;

import org.junit.Test;

import io.moo.propane.exception.NoComponentIdFoundException;

/**
 * Test case for component id extraction.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class DefaultComponentIdExtractorTest {
  @Test
  public void testExtractComponentId() {
    final DefaultComponentIdExtractor extractor = new DefaultComponentIdExtractor();
    final Collection<String> extracted = extractor.extract("classpath://foo/bar.props");
    assertThat(extracted, notNullValue());
    assertThat(extracted, hasItem("bar"));
  }


  @Test(expected = NoComponentIdFoundException.class)
  public void testExtractComponentIdWithBlankPropertyName() {
    new DefaultComponentIdExtractor().extract("");
  }


  @Test(expected = NoComponentIdFoundException.class)
  public void testExtractComponentIdWithNullPropertyName() {
    new DefaultComponentIdExtractor().extract(null);
  }


  @Test
  public void testExtractComponentIdWithoutFileExtension() {
    final DefaultComponentIdExtractor extractor = new DefaultComponentIdExtractor();
    final Collection<String> extracted = extractor.extract("classpath://foo/bar");
    assertThat(extracted, notNullValue());
    assertThat(extracted, hasItem("bar"));
  }


  @Test
  public void testExtractComponentIdWithLeadingSpecialChars() {
    final DefaultComponentIdExtractor extractor = new DefaultComponentIdExtractor();
    final Collection<String> extracted = extractor.extract("classpath://f.o.o/bar");
    assertThat(extracted, notNullValue());
    assertThat(extracted, hasItem("bar"));
  }


  @Test
  public void testFail() {
    final DefaultComponentIdExtractor extractor = new DefaultComponentIdExtractor();
    final Collection<String> extracted = extractor.extract("classpath://configurations/test1.properties");
    assertThat(extracted, notNullValue());
    assertThat(extracted, hasItem("test1"));
  }
}
