package io.moo.propane;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import io.moo.propane.data.PropertiesEntity;

/**
 * Created by bagdemir on 14/03/15.
 */
public class DirectoryPropertiesProviderImpl implements PropertiesProvider {
  private static final String[] FILE_PATTERNS = new String[]{"cfg", "properties"};


  public Collection<PropertiesEntity> read() {
    return null;
  }


  public Collection<PropertiesEntity> read(final String rootContext) {
    return read(rootContext, false);
  }


  public Collection<PropertiesEntity> read(final String rootContext, final boolean recursive) {
    final File root = new File(rootContext);
    final Collection<File> files = FileUtils.listFiles(root, FILE_PATTERNS, recursive);
    for (File file : files) {
      System.out.println(file);
    }

    return null;
  }


  @Override
  public PropertiesProvider init() {
    return null;
  }


  @Override
  public Object take() {
    return null;
  }
}
