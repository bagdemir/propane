package io.moo.propane.providers;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.moo.propane.sources.ConfigData;
import io.moo.propane.sources.ConfigurationSource;

/**
 * Calls the configurations from the source within a schedule.
 */
public abstract class ScheduledConfigurationProvider<T> implements ConfigurationProvider<T> {

  private static final int CORE_POOL_SIZE = 1;
  private static final long INITIAL_DELAY = 0L;
  private static final int DEFAULT_REFRESH_RATE_IN_SECS = 60;

  private int frequencyInSecs = DEFAULT_REFRESH_RATE_IN_SECS;
  private ConfigurationSource connector;
  private Class<T> configurationClazz;
  private ConfigurationFilter filter = new ConfigurationEnvironmentFilter();

  private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

  protected final AtomicReference<Optional<ConfigData>> configData = new
          AtomicReference<>(Optional.empty());


  public ScheduledConfigurationProvider(final Class<T> propsClazz,
                                        final ConfigurationSource connector,
                                        final int frequencyInSecs) {

    this.configurationClazz = propsClazz;
    this.connector = connector;
    this.frequencyInSecs = frequencyInSecs;

    initScheduler();
  }


  protected void initScheduler() {

    final Runnable reader = () -> {
      ConfigData configuration = connector.read(configurationClazz).get();
      ConfigData filteredConfiguration = filter.filter(configuration);
      configData.set(Optional.of(filteredConfiguration));
    };

    // Submits a new task to call the configurations.
    executorService.scheduleAtFixedRate(reader, INITIAL_DELAY, frequencyInSecs, TimeUnit.SECONDS);
  }


  public Class<T> getConfigurationClazz() {
    return configurationClazz;
  }
}
