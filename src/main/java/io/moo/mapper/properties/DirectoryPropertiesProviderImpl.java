package io.moo.mapper.properties;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import io.moo.mapper.properties.data.PropertiesEntity;

/**
 * Created by bagdemir on 14/03/15.
 */
public class DirectoryPropertiesProviderImpl implements PropertiesProvider {
  private static final String[] FILE_PATTERNS = new String[]{"cfg", "properties"};


  @Override
  public Collection<PropertiesEntity> read() {
    return null;
  }


  @Override
  public Collection<PropertiesEntity> read(final String rootContext) {
    return read(rootContext, false);
  }


  @Override
  public Collection<PropertiesEntity> read(final String rootContext, final boolean recursive) {
    final File root = new File(rootContext);
    final Collection<File> files = FileUtils.listFiles(root, FILE_PATTERNS, recursive);
    for (File file : files) {
      System.out.println(file);
    }

    return null;
  }
}
