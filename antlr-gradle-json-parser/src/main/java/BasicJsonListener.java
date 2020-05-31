import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class BasicJsonListener {
  public static void main(String... args) {
    final var lexer = new JSONLexer(CharStreams.fromString("{\"name\": \"Elvis\", \"age\": 24}"));
    final var tokens = new CommonTokenStream(lexer);
    final var parser = new JSONParser(tokens);

    final var valueListener = new JsonValueListener();
    ParseTreeWalker.DEFAULT.walk(valueListener, parser.json());
  }

  static class JsonValueListener extends JSONBaseListener {
    @Override
    public void enterValue(JSONParser.ValueContext ctx) {
      System.out.printf("txt=%s%n", ctx.getText());
    }
  }

}
