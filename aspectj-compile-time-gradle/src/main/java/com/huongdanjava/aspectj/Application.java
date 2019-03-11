package com.huongdanjava.aspectj;

import com.ea.agentloader.AgentLoader;

public class Application {

	public static void main(String[] args) throws Throwable {
		AgentLoader.loadAgentClass("org.aspectj.weaver.loadtime.Agent", "");
		new Application().run();
	}

	private void run() {
		HelloWorld hello = new HelloWorld();
		System.out.printf("msg=%s%n", hello.hello("Joao"));
	}

}
