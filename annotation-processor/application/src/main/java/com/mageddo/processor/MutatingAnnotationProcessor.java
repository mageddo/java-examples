package com.mageddo.processor;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeTranslator;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.util.Set;


@SupportedAnnotationTypes("com.mageddo.processor.Immutable")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class MutatingAnnotationProcessor extends AbstractProcessor {
	private Trees trees;

	@Override
	public void init(ProcessingEnvironment processingEnv) {
		try {
			super.init(processingEnv);
			trees = Trees.instance(processingEnv);
		} catch (Exception e){
			System.out.println(">>>>>" + e.getMessage());
			throw e;
		}
	}

	@Override
	public boolean process(final Set<? extends TypeElement> annotations,
												 final RoundEnvironment roundEnv) {
		System.out.println("MutatingAnnotationProcessor: init");
		final TreePathScanner<Object, CompilationUnitTree> scanner =
			new TreePathScanner<Object, CompilationUnitTree>() {
				@Override
				public Trees visitClass(final ClassTree classTree, final CompilationUnitTree unitTree) {
					if (unitTree instanceof JCCompilationUnit) {
						final JCCompilationUnit compilationUnit = (JCCompilationUnit) unitTree;

						// Only process on files which have been compiled from source
						if (compilationUnit.sourcefile.getKind() == JavaFileObject.Kind.SOURCE) {
							compilationUnit.accept(new TreeTranslator() {
								public void visitVarDef(final JCVariableDecl tree) {
									super.visitVarDef(tree);

									if ((tree.mods.flags & Flags.FINAL) == 0) {
										tree.mods.flags |= Flags.FINAL;
									}
								}
							});
						}
					}

					return trees;
				}
			};

		for (final Element element : roundEnv.getElementsAnnotatedWith(Immutable.class)) {
			System.out.println("found annotation");
			final TreePath path = trees.getPath(element);
			scanner.scan(path, path.getCompilationUnit());
		}

		// Claiming that annotations have been processed by this processor 
		return true;
	}
}
