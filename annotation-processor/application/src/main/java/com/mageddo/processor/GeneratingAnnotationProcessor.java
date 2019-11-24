package com.mageddo.processor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class GeneratingAnnotationProcessor extends AbstractProcessor {

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		System.out.println("GeneratingAnnotationProcessor: init");
		super.init(processingEnv);
	}

	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {

		System.out.println("generating immutable class");
		for (final Element element : roundEnv.getElementsAnnotatedWith(Immutable.class)) {
			if (element instanceof TypeElement) {
				final TypeElement typeElement = (TypeElement) element;
				final PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();

				try {
					final String className = typeElement.getSimpleName() + "Immutable";
					final JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(
						packageElement.getQualifiedName() + "." + className);

					try (Writer writter = fileObject.openWriter()) {
						writter.append("package " + packageElement.getQualifiedName() + ";");
						writter.append("\n\n");
						writter.append("public class " + className + " {");
						writter.append("\n");
						writter.append("}");
					}
				} catch (final IOException ex) {
					processingEnv.getMessager().printMessage(Kind.ERROR, ex.getMessage());
				}
			}
		}

		// Claiming that annotations have been processed by this processor 
		return true;
	}
}
