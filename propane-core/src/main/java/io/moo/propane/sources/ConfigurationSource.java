/**
 * The MIT License (MIT) <p> Copyright (c) 2015 moo.io , Erhan Bagdemir <p> Permission is hereby
 * granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions: <p> The above copyright notice and this permission
 * notice shall be included in all copies or substantial portions of the Software. <p> THE SOFTWARE
 * IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.moo.propane.sources;

import java.util.Optional;
import java.util.concurrent.Callable;

import io.moo.propane.exception.InvalidConfigurationEntityException;

/**
 * @author bagdemir
 * @version 1.0
 * @since 1.0
 */
public abstract class ConfigurationSource implements Callable<Optional<ConfigData>> {
    static final String CLASSPATH_PREFIX = "classpath://";
    static final String FILE_PREFIX = "file://";
    static final String DB_MYSQL_PREFIX = "mysql://";
    static final String BLANK_STR = "";

    protected final String source;
    protected final Class<?> entityType;


    /**
     * Static factory method that creates new source implementations for a source annotation given.
     *
     * @param url        The source URL
     * @param entityType Entity type of which the entity instances will be created from the source.
     * @return {@link ConfigurationSource} instance.
     */
    public static ConfigurationSource of(
            final String url,
            final Class<?> entityType) {

        if (url == null) {
            throw new InvalidConfigurationEntityException("@Source annotation is missing.");
        }

        final String prefix = getPrefix(url);

        ConfigurationSource configSource;

        switch (prefix) {
            case "classpath":
                configSource = new ClasspathFileConfigurationSource(url.replace
                        (CLASSPATH_PREFIX, BLANK_STR), entityType);
                break;
            default:
                throw new RuntimeException("Unknown source type.");
        }

        return configSource;
    }


    private static String getPrefix(String url) {
        final String[] split = url.split(":");
        if (split.length == 0) {
            throw new RuntimeException("Source URL doesn't seem valid.");
        }
        return split[0];
    }


    public ConfigurationSource(final String source, final Class<?> entityType) {
        this.source = source;
        this.entityType = entityType;
    }


    @Override
    public Optional<ConfigData> call() throws Exception {
        return read(entityType);
    }


    /**
     * Reads properties from the resource.
     *
     * @return Properties.
     */
    public abstract <T> Optional<ConfigData> read(Class<T> clazz);


    public Class<?> getEntityType() {
        return entityType;
    }


    public String getSource() {
        return source;
    }
}
