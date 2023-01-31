package com.mageddo.commonscli.git;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Git {

  private final DefaultParser parser;
  private final CommandLine commandLine;

  public Git(DefaultParser parser, CommandLine commandLine) {
    this.parser = parser;
    this.commandLine = commandLine;
  }

  public static Git parse(String[] args) throws ParseException {
    // definition
    final var options = new Options();
    options.addOption("status", "git-status - Show the working tree status");

    // parse
    final var parser = new DefaultParser();
    final var commandLine = parser.parse(options, args);

    return new Git(parser, commandLine);
  }

}
