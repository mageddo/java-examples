package com.mageddo.tomcat;

import com.mageddo.dao.DatabaseBuilderDao;
import com.mageddo.utils.ZipUtils;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.JarResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.springframework.util.ClassUtils;

import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.mageddo.utils.Utils.getDeployPath;

public class TomcatEmbeddedServer {

	public Tomcat run() throws IOException, ServletException, LifecycleException {

		final String webAppMount = "/WEB-INF/classes";
		final String ctxPath = "/";
		final String internalPath = "/";
		final String deployPath = getDeployPath();
		final Path baseDir;

		if(ZipUtils.runningInsideJar()){
			baseDir = Files.createTempDirectory("tomcat");
			// cópia de dentro do jar ainda nao funciona
			// ZipUtils.copyFromCurrentJar("webapp", baseDir);
		} else {
			baseDir = Paths.get(deployPath);
		}

		final Path docBase = baseDir.resolve("webapp");
		Files.createDirectories(docBase);

		final Tomcat tomcat = new Tomcat();
		tomcat.setBaseDir(String.valueOf(baseDir));

		final Context ctx = tomcat.addWebapp(ctxPath, String.valueOf(docBase));
		ctx.setParentClassLoader(ClassUtils.getDefaultClassLoader());
		final WebResourceRoot resources = new StandardRoot(ctx);

		if(ZipUtils.runningInsideJar()){

			resources.addPreResources(new JarResourceSet(
				resources, webAppMount, deployPath, internalPath
			));
		} else {
			resources.addPreResources(new DirResourceSet(
				resources, webAppMount, deployPath, internalPath
			));
		}

		ctx.setResources(resources);

		tomcat.setPort(9095);
		tomcat.start();

		return tomcat;
	}
}
