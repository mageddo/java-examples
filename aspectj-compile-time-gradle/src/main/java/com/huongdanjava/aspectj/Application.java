package com.huongdanjava.aspectj;

import com.huongdanjava.aspectj.javaagent.AspectJUtils;

public class Application {

	public static void main(String[] args) throws Throwable {
		AspectJUtils.loadAgent();
		new Application().run();
	}

	private void run() {
		HelloWorld hello = new HelloWorld();
		System.out.printf("msg=%s%n", hello.hello("Joao"));
	}

}
