import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Ex01FilledDependencies {
  public static void main(String... args) throws IOException {
    final var resource = Ex01FilledDependencies.class.getResourceAsStream("/example01.gradle");
    final var lexer = new GradleLexer(CharStreams.fromStream(resource));
    final var tokens = new CommonTokenStream(lexer);
    final var parser = new GradleParser(tokens);

    final var valueListener = new JsonValueListener();
    ParseTreeWalker.DEFAULT.walk(valueListener, parser.gradle());
  }

  static class JsonValueListener extends GradleBaseListener {
    @Override
    public void enterValue(GradleParser.ValueContext ctx) {
      System.out.printf("txt=%s%n", ctx.getText());
    }
  }
}
