package com.mageddo.servlet.tomcat;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/helloWorld")
public class HelloWorldServlet extends HttpServlet {

	public static String GET_MESSAGE = "Hello from get from " + HelloWorldServlet.class.getSimpleName();
	public static String POST_MESSAGE = "Hello from post from " + HelloWorldServlet.class.getSimpleName();

	@Override
	public String getServletName() {
		return "helloWorld";
	}

	@Override
	public ServletConfig getServletConfig() {
		return super.getServletConfig();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getOutputStream().println(GET_MESSAGE);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getOutputStream().println(POST_MESSAGE);
	}
	
	
	
}
