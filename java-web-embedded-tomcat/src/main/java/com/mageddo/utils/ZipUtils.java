package com.mageddo.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

public final class ZipUtils {
	private ZipUtils() {
	}

	public static void copyFromCurrentJar(String sourceRelativePathInTheJar, final Path target) throws IOException {
		copyFromJar(getCurrentJarPath(), sourceRelativePathInTheJar, target);
	}
	
	public static void copyFromJar(URI jarURI, String source, final Path target) throws IOException {
		final FileSystem fileSystem = FileSystems.newFileSystem(jarURI, new HashMap<>());
		final Path jarPath = fileSystem.getPath(source);
		Files.walkFileTree(jarPath, new SimpleFileVisitor<Path>() {
			private Path currentTarget;
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				currentTarget = target.resolve(jarPath.relativize(dir).toString());
				Files.createDirectories(currentTarget);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.copy(file, target.resolve(jarPath.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	private static URI getCurrentJarPath() {
		try {
			return ZipUtils.class.getResource("").toURI();
		} catch (URISyntaxException e){
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static boolean runningInsideJar(){
		return Utils.getDeployPath().endsWith(".jar");
	}
}
