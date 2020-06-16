package com.mageddo.jvmti;

import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Value
public class NativeLibrary {

  String path;

  public InputStream getOriginalFileStream() {
    return JvmtiNativeLibraryFinder.class.getResourceAsStream(this.path);
  }

  @SneakyThrows
  public Path installAtTempPath(){
    final Path tmpPath = SystemUtils
      .getJavaIoTmpDir()
      .toPath()
      .resolve("jvmti-class-agent")
      .resolve(path);

    Files.createDirectories(tmpPath.getParent());

    try(
      InputStream source = this.getOriginalFileStream();
      OutputStream target = Files.newOutputStream(tmpPath)
    ){
      IOUtils.copy(source, target);
    }
    return tmpPath;
  }
}
