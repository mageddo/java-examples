package com.mageddo.jcommander.git;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.List;

@Parameters(commandDescription = "Get commit status for files")
public class GitStatusCommand implements Command {

	@Parameter(description = "Paths to get status")
	private List<String> paths;

	@Override
	public void run() {
		System.out.printf("git status command running, parameters=%s\n", this);
	}

	@Override
	public String toString() {
		return "GitStatusCommand{" +
			"paths=" + paths +
			'}';
	}
}
