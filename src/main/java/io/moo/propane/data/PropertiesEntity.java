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
package io.moo.propane.data;

import java.util.Arrays;

/**
 * Entity to hold a single property imported from a props file. Each property
 * is associated to a component, indicated by <tt>componentId</tt> and may
 * belong to more than one context. A context of a property may refer to
 * a user defined scope, like region, environment, etc.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class PropertiesEntity<T> {
  private String componentId;
  private String[] contextIds;
  private String propertyName;
  private T propertyValue;


  public String getComponentId() {
    return componentId;
  }


  public void setComponentId(final String componentId) {
    this.componentId = componentId;
  }


  public String[] getContextIds() {
    return contextIds;
  }


  public void setContextIds(final String[] contextIds) {
    this.contextIds = contextIds;
  }


  public String getPropertyName() {
    return propertyName;
  }


  public void setPropertyName(final String propertyName) {
    this.propertyName = propertyName;
  }


  public T getPropertyValue() {
    return propertyValue;
  }


  public void setPropertyValue(final T propertyValue) {
    this.propertyValue = propertyValue;
  }


  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof PropertiesEntity)) return false;

    PropertiesEntity that = (PropertiesEntity) o;

    if (!componentId.equals(that.componentId)) return false;
    if (!Arrays.equals(contextIds, that.contextIds)) return false;
    if (!propertyName.equals(that.propertyName)) return false;
    if (!propertyValue.equals(that.propertyValue)) return false;

    return true;
  }


  @Override
  public int hashCode() {
    int result = componentId.hashCode();
    result = 31 * result + Arrays.hashCode(contextIds);
    result = 31 * result + propertyName.hashCode();
    result = 31 * result + propertyValue.hashCode();
    return result;
  }


  @Override
  public String toString() {
    return "PropertiesEntity{" +
            "componentId='" + componentId + '\'' +
            ", contextIds=" + Arrays.toString(contextIds) +
            ", propertyName='" + propertyName + '\'' +
            ", propertyValue=" + propertyValue +
            '}';
  }
}
