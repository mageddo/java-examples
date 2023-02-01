package com.mageddo.picocli.git;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

import lombok.Getter;
import lombok.ToString;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Getter
@Command(
    name = "git",
    mixinStandardHelpOptions = true,
    version = "1.0",
    description = "Git dummy command",
    subcommands = {Git.Status.class}
)
public class Git {

  @Getter
  @Command(
      name = "status",
      description = "Show the working tree status",
      showDefaultValues = true
  )
  @ToString
  public static class Status implements Callable<Status> {

//    @CommandLine.Option(names = {"--paths"}, defaultValue = "batata")
    @CommandLine.Parameters(defaultValue = "batata")
    private List<Path> paths;

    @Override
    public Status call() {
      System.out.println("called!");
      return this;
    }
  }

  public static Status parse(String[] args){
    final var cmd = new CommandLine(new Git());
    assert cmd.execute(args) == 0;
    return cmd.getSubcommands().get("status").getCommand();
  }

  public static void main(String[] args) {
    final var v = parse(new String[]{"status", "--paths", "/tmp"});

    System.out.println(v);
  }
}
