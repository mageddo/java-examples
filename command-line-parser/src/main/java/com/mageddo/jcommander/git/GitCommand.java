package com.mageddo.jcommander.git;

import com.beust.jcommander.JCommander;

public class GitCommand implements Command {

	static JCommander buildCommander() {
		return JCommander
			.newBuilder()
			.addCommand("status", new GitStatusCommand())
			.build();
	}

	public static void parseAndRun(String... args){
		JCommander jCommander = buildCommander();
		jCommander.parse(args);

		for (final Object object : jCommander.getCommands().get(jCommander.getParsedCommand()).getObjects()) {
			Command.class.cast(object).run();
		}
	}

	@Override
	public void run() {
		System.out.println("git command running.....");
	}
}
