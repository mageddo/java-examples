package com.mageddo.jvmti;

import lombok.SneakyThrows;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.apache.commons.lang3.Validate;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CurrentJarLoader {

  /**
   * Loads the current running jar into the specified process
   */
  public void load(int pid) {
    final Path jarPath = getCurrentJar();
    Validate.isTrue(jarPath.toString().endsWith(".jar"), "Not running in a jar: %s", jarPath);
    ByteBuddyAgent.attach(jarPath.toFile(), String.valueOf(pid));
  }

  @SneakyThrows
  private Path getCurrentJar() {
    return Paths.get(CurrentJarLoader.class
      .getProtectionDomain()
      .getCodeSource()
      .getLocation()
      .toURI()
    );
  }
}
