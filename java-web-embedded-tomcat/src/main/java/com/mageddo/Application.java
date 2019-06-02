package com.mageddo;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;

import com.mageddo.dao.DatabaseBuilderDao;
import com.mageddo.tomcat.TomcatEmbeddedServer;
import com.mageddo.utils.ZipUtils;
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

	public static void main(String[] args) throws LifecycleException, ServletException, IOException {

		SpringEnvSingleton.prepareEnv(args);
		final Environment env = SpringEnvSingleton.getEnv();
		new DatabaseBuilderDao().buildDatabase();

		final Tomcat tomcat = new TomcatEmbeddedServer().run();
		tomcat.getServer().await();
	}


}
