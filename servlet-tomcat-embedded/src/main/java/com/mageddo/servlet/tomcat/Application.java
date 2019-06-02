package com.mageddo.servlet.tomcat;

import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;

import java.nio.file.Files;

public class Application {
	public static void main(String[] args) throws Exception {

//		WebAppContext context = new WebAppContext();


		StandardServer server = new StandardServer();



//		server.setPort(8080);
//		server.storeContext();
//		server.start();
//		server.await();
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
//		tomcat.addWebapp("", String.valueOf(Files.createTempDirectory("tomcat").toString()));
		tomcat.addContext("", String.valueOf(Files.createTempDirectory("tomcat").toString()));

//		tomcat.getHost().findChildren()[0].add;

		tomcat.addServlet("", "helloworld", new HelloWorldServlet());
//		ctx.getServletContext().addServlet("helloworld", new HelloWorldServlet());
		tomcat.setBaseDir(String.valueOf(Files.createTempDirectory("tomcat").toString()));
		tomcat.start();
		tomcat.getServer().setPort(8080);
		tomcat.getServer().await();
	}
}
