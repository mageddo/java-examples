package com.mageddo.jcommander.git;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class GitCommandTest {

	@Test
	public void buildCommander() {

		final ByteArrayOutputStream bout = new ByteArrayOutputStream();

		// arrange
		System.setOut(new PrintStream(bout));

		// act
		GitCommand.parseAndRun("status", "do.txt", "stuff.txt");

		// assert
		assertEquals(
			"git status command running, parameters=GitStatusCommand{paths=[do.txt, stuff.txt]}\n",
			bout.toString()
		);


	}
}
