package com.mageddo;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletException;

import com.mageddo.dao.DatabaseBuilderDao;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.JarResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.springframework.core.env.Environment;
import org.springframework.profile.SpringEnvSingleton;
import org.springframework.util.ClassUtils;

import static com.mageddo.utils.Utils.getDeployPath;

public class Application {

	public static void main(String[] args) throws LifecycleException, ServletException, IOException, URISyntaxException {

		SpringEnvSingleton.prepareEnv(args);
		final Environment env = SpringEnvSingleton.getEnv();
		
		new DatabaseBuilderDao().buildDatabase();

		final Tomcat tomcat = new Tomcat();
		final String ctxPath = "/";
		final String docBase = ClassUtils.getDefaultClassLoader().getResource("webapp").getFile();
// v2
//		final StandardContext context = new StandardContext();
//		context.setName(ctxPath);
//		context.setPath(docBase);
//		context.addLifecycleListener(new Tomcat.FixContextListener());
//		context.setParentClassLoader(ClassUtils.getDefaultClassLoader());
//		final WebappLoader loader = new WebappLoader(context.getParentClassLoader());
//		loader.setLoaderClass(WebappClassLoader.class.getName());
//
//		context.setLoader(loader);
//		tomcat.getHost().addChild(context);

// v3
		final Context ctx = tomcat.addWebapp(ctxPath, "/tmp");
		ctx.setParentClassLoader(ClassUtils.getDefaultClassLoader());
//		final CustomerController customerServlet = new CustomerController();
//		tomcat.addServlet(ctxPath, customerServlet.getClass().getSimpleName(), customerServlet);

// v1
//		final String docBase = "/home/system/Dropbox/dev/projects/spring-boot-mvc-jdbc-template/build/classes";
//		tomcat.addContext(ctxPath, docBase);
//		tomcat.addWebapp(tomcat.getHost(), "/tmp", docBase);
//		final CustomerController customerServlet = new CustomerController();
//		tomcat.addServlet(ctxPath, customerServlet.getClass().getSimpleName(), customerServlet);
//
		WebResourceRoot resources = new StandardRoot(ctx);

		final String deployPath = getDeployPath();
		if(deployPath.endsWith(".jar")){
			resources.addPreResources(new JarResourceSet(
				resources, "/WEB-INF/classes",
				deployPath,
				"/"
			));
		} else {
			resources.addPreResources(new DirResourceSet(
				resources, "/WEB-INF/classes",
				deployPath,
				"/"
			));
		}

//		resources.addJarResources(new JarResourceSet());
		ctx.setResources(resources);

		tomcat.setPort(9095);
		tomcat.start();

		System.out.println(ManagementFactory.getRuntimeMXBean().getName());
		tomcat.getServer().await();

	}


}
