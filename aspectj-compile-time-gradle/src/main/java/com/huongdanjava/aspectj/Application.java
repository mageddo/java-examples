package com.huongdanjava.aspectj;

public class Application {

	public static void main(String[] args) throws Throwable {
		new Application().run();
	}

	private void run() {
		HelloWorld hello = new HelloWorld();
		System.out.printf("msg=%s%n", hello.hello("Joao"));
	}

}
