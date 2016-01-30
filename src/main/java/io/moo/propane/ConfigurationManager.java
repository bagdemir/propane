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
package io.moo.propane;

import io.moo.propane.data.ContextInfo;

import java.util.Optional;

/**
 * {@link ConfigurationManager} is the repository for your configuration
 * entities which lives in a context like environment, region, etc. According
 * to its content, the configuration managers gives only the configurations
 * back that they exist within the same configuration.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public interface ConfigurationManager {
  <T> boolean register(Class<T> clazz);

  <T> boolean isRegistered(Class<T> clazz);

  <T> Optional<T> load(Class<T> clazz);

  <T> Optional<T> load(Class<T> clazz, Optional<ContextInfo> contexts);

  static ConfigurationManager newManager(Optional<ContextInfo> contextInfo) {
    return new ConfigurationManagerImpl(contextInfo);
  }

}
