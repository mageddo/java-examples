import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Ex02FilledDependencies {
  public static void main(String... args) throws IOException {
    final var resource = Ex02FilledDependencies.class.getResourceAsStream("/example02.gradle");
    final var lexer = new GradleLexer(CharStreams.fromStream(resource));
    final var tokens = new CommonTokenStream(lexer);
    final var parser = new GradleParser(tokens);

    final var valueListener = new JsonValueListener();
    ParseTreeWalker.DEFAULT.walk(valueListener, parser.gradle());
  }

  static class JsonValueListener extends GradleBaseListener {
    @Override
    public void enterDependency(GradleParser.DependencyContext ctx) {
      System.out.printf("enterDependency=%s%n", ctx.getText());
    }
    @Override
    public void enterDependencies(GradleParser.DependenciesContext ctx) {
      System.out.printf("enterDependencies=%s%n", ctx.getText());
    }

  }
}
