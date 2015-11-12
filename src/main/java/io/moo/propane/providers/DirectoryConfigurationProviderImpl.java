package io.moo.propane.providers;

import java.io.File;
import java.util.Collection;

import io.moo.propane.data.ConfigurationEntity;
import org.apache.commons.io.FileUtils;

/**
 * Created by bagdemir on 14/03/15.
 */
public class DirectoryConfigurationProviderImpl implements ConfigurationProvider {
  private static final String[] FILE_PATTERNS = new String[]{"cfg", "properties"};


  public Collection<ConfigurationEntity> read() {
    return null;
  }


  public Collection<ConfigurationEntity> read(final String rootContext) {
    return read(rootContext, false);
  }


  public Collection<ConfigurationEntity> read(final String rootContext, final boolean recursive) {
    final File root = new File(rootContext);
    final Collection<File> files = FileUtils.listFiles(root, FILE_PATTERNS, recursive);
    for (File file : files) {
      System.out.println(file);
    }

    return null;
  }

  @Override
  public Object load() {
    return null;
  }
}
