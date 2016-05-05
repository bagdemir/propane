/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 moo.io , Erhan Bagdemir
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.moo.propane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import io.moo.propane.annotation.Configuration;
import io.moo.propane.annotation.processor.AnnotationUtils;
import io.moo.propane.data.ContextInfo;
import io.moo.propane.exception.InvalidConfigurationEntityException;
import io.moo.propane.providers.ConfigurationProvider;

/**
 * {@link ConfigurationManagerImpl} is the repository for your configuration entities which lives in
 * a context like environment, region, etc. According to its content, the configuration managers
 * gives only the configurations back that they exist within the same configuration.
 *
 * @author bagdemir
 * @version 1.0
 */
public class ConfigurationManagerImpl implements ConfigurationManager {

    private static final Logger LOG = LogManager.getLogger();
    private static final int DEFAULT_REFRESH_RATE = 60;

    private final Optional<ContextInfo> contextInfo;
    private final Optional<Integer> refreshRate;


    public ConfigurationManagerImpl(
            final Optional<ContextInfo> contextInfo,
            final Optional<Integer> refressness) {
        this.contextInfo = contextInfo;
        this.refreshRate = refressness;
    }


    // TODO type checking
    private final Map<Class<?>, ConfigurationProvider> cache = new ConcurrentHashMap<>();


    @Override
    public <T> boolean register(final Class<T> clazz) {

        if (clazz == null || cache.containsKey(clazz)) {
            return false;
        }

        validateConfigurationEntity(clazz);
        registerConfigurationProvider(clazz);

        return true;
    }


    private <T> void registerConfigurationProvider(final Class<T> clazz) {
        Integer refreshRate = this.refreshRate.orElse(DEFAULT_REFRESH_RATE);
        ConfigurationProvider<T> provider = ConfigurationProvider.create(clazz, AnnotationUtils.getSourceURL(clazz), refreshRate);
        cache.put(clazz, provider);
    }


    @Override
    public <T> boolean isRegistered(final Class<T> clazz) {

        return cache.containsKey(clazz);
    }


    @Override
    public <T> Optional<T> load(final Class<T> clazz) {

        return load(clazz, Optional.empty());
    }


    @Override
    public <T> Optional<T> load(
            final Class<T> clazz,
            final Optional<ContextInfo> contextInfo) {

        if (isRegistered(clazz)) {
            //TODO
            return cache.get(clazz).load(clazz, contextInfo);
        }
        return Optional.empty();
    }


    public Optional<ContextInfo> getContextInfo() {
        return contextInfo;
    }


    private <T> void validateConfigurationEntity(final Class<T> clazz) {

        Optional.ofNullable(clazz.getAnnotation(Configuration.class)).
                orElseThrow(() -> new InvalidConfigurationEntityException
                        ("@Configuration annotation is missing."));

    }
}
