package com.mageddo.commonscli;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class SimpleBooleanArgs {

  private final Options options;
  private final CommandLine cmd;

  public SimpleBooleanArgs(Options options, CommandLine cmd) {
    this.options = options;
    this.cmd = cmd;
  }

  public static SimpleBooleanArgs parse(String args[]) throws ParseException {
    // definition
    final var options = new Options();
    options.addOption("t", false, "display current time");
    options.addOption("h", "Help");

    // parsing
    final var parser = new DefaultParser();
    final var cmd = parser.parse(options, args);

    return new SimpleBooleanArgs(options, cmd);
  }

  public boolean isTrue() {
    return this.cmd.hasOption('t');
  }

  public String help() {
    final var sw = new StringWriter();
    if (this.cmd.hasOption("h")) {
      final var formatter = new HelpFormatter();
      final var w = new PrintWriter(sw);
      formatter.printHelp(
          w, HelpFormatter.DEFAULT_WIDTH,
          "koeh", null, this.options,
          HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD,
          null
      );
    }
    return sw.toString();
  }

}
