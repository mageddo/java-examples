package com.mageddo.java11.flightrecorder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;

public class HelloWorldConsumer {
  public void consume() throws IOException {
    final Path p = Paths.get("recording.jfr");
    for (RecordedEvent e : RecordingFile.readAllEvents(p)) {
      System.out.println(e.getStartTime() + " : " + e.getValue("message"));
    }
  }
}
