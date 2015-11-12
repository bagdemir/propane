package io.moo.propane.providers;

import io.moo.propane.sources.ConfigData;
import io.moo.propane.sources.ConfigurationSource;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Calls the configurations from the source within a schedule.
 */
public abstract class ScheduledConfigurationProvider<T> implements ConfigurationProvider<T> {
  private static final int CORE_POOL_SIZE = 1;
  private static final long INITIAL_DELAY = 0L;

  private int frequencyInMins = 1;
  private ConfigurationSource connector;
  private Class<T> configurationClazz;

  private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
  protected final AtomicReference<ConfigData> configData = new AtomicReference<>(new ConfigData());

  public ScheduledConfigurationProvider(final Class<T> propsClazz, final ConfigurationSource connector) {
    this.configurationClazz = propsClazz;
    this.connector = connector;

    initScheduler();
  }

  protected void initScheduler() {
    // Submits a new task to call the configurations.
    executorService.scheduleAtFixedRate(() -> {
      final ConfigData newConfigData = connector.read();
      configData.set(newConfigData);
    }, INITIAL_DELAY, frequencyInMins, TimeUnit.MINUTES);
  }

  public Class<T> getConfigurationClazz() {
    return configurationClazz;
  }
}
