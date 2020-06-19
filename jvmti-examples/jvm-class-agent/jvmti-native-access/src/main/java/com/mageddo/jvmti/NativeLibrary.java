package com.mageddo.jvmti;

import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Slf4j
@Value
public class NativeLibrary {

  String path;

  public InputStream getOriginalFileStream() {
    return Objects.requireNonNull(
      JvmtiNativeLibraryFinder.class.getResourceAsStream(this.path),
      "Library not found at jar resources: " + this.path
    );
  }

  /**
   * Copy the library from jar into a temp path,
   * then this library can be loaded to some JVM
   */
  @SneakyThrows
  public Path installAtTempPath(){
    final Path tmpPath = SystemUtils
      .getJavaIoTmpDir()
      .toPath()
      .resolve("jvmti-class-agent")
      .resolve(path.startsWith("/") ? path.substring(1) : path);
    log.debug("installing at {}", tmpPath);
    if(Files.exists(tmpPath)){
      log.warn("status=already-installed, path={}", tmpPath);
      return tmpPath;
    }
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
