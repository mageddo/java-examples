package com.mageddo.commonscli;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

public class SimpleBooleanArgs {
  public static boolean isTrue(String[] args) throws Exception {

    // definition
    final var options = new Options();
    options.addOption("t", false, "display current time");

    // parsing
    final var parser = new DefaultParser();
    final var cmd = parser.parse(options, args);

    // interrogation
    return cmd.hasOption('t');
  }
}
