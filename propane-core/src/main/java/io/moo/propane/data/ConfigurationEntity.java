/**
 * The MIT License (MIT) <p/> Copyright (c) 2015 moo.io , Erhan Bagdemir <p/> Permission is hereby
 * granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions: <p/> The above copyright notice and this permission
 * notice shall be included in all copies or substantial portions of the Software. <p/> THE SOFTWARE
 * IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.moo.propane.data;

import java.util.Collection;
import java.util.Set;

/**
 * Entity to hold a single property imported from a props file. Each property is associated to a
 * component, indicated by <tt>componentId</tt> and may belong to more than one context. A context
 * of a property may refer to a user defined scope, like region, environment, etc.
 *
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public class ConfigurationEntity {
    private String componentId;
    private Set<String> contextIds;
    private String propertyName;
    private Object propertyValue;


    public ConfigurationEntity(
            final String componentId,
            final Set<String> contextIds,
            final String propertyName,
            final Object propertyValue) {

        this.componentId = componentId;
        this.contextIds = contextIds;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }


    public String getComponentId() {
        return componentId;
    }


    public void setComponentId(final String componentId) {
        this.componentId = componentId;
    }


    public Collection<String> getContextIds() {
        return contextIds;
    }


    public void setContextIds(final Set<String> contextIds) {
        this.contextIds = contextIds;
    }


    public String getPropertyName() {
        return propertyName;
    }


    public void setPropertyName(final String propertyName) {
        this.propertyName = propertyName;
    }


    public Object getPropertyValue() {
        return propertyValue;
    }


    public void setPropertyValue(final Object propertyValue) {
        this.propertyValue = propertyValue;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigurationEntity)) return false;

        ConfigurationEntity that = (ConfigurationEntity) o;

        if (componentId != null ? !componentId.equals(that.componentId) : that.componentId != null)
            return false;
        if (contextIds != null ? !contextIds.equals(that.contextIds) : that.contextIds != null)
            return false;
        if (propertyName != null ? !propertyName.equals(that.propertyName) : that.propertyName != null)
            return false;
        return !(propertyValue != null ? !propertyValue.equals(that.propertyValue) : that.propertyValue != null);

    }


    @Override
    public int hashCode() {
        int result = componentId != null ? componentId.hashCode() : 0;
        result = 31 * result + (contextIds != null ? contextIds.hashCode() : 0);
        result = 31 * result + (propertyName != null ? propertyName.hashCode() : 0);
        result = 31 * result + (propertyValue != null ? propertyValue.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Configuration{" +
                "componentId='" + componentId + '\'' +
                ", contextIds=" + String.join(",", getContextIds()) +
                ", propertyName='" + propertyName + '\'' +
                ", propertyValue=" + propertyValue +
                '}';
    }
}
