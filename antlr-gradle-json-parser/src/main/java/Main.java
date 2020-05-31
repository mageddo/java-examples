import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Main {
  public static void main(String[] args) {
    final var lexer = new JSONLexer(CharStreams.fromString("{\"name\": \"Elvis\", \"age\": 24}"));
    final var tokens = new CommonTokenStream(lexer);
    final var parser = new JSONParser(tokens);

    final var replaceExpr = new ReplaceExpr(tokens);
    ParseTreeWalker.DEFAULT.walk(replaceExpr, parser.json());
  }

  static class ReplaceExpr extends JSONBaseListener {

    private final TokenStreamRewriter rewriter;

    public ReplaceExpr(CommonTokenStream tokens) {
      this.rewriter = new TokenStreamRewriter(tokens);
    }
    public String getReplacedCode() {
      return this.rewriter.getText();
    }

    @Override
    public void enterValue(JSONParser.ValueContext ctx) {
      System.out.printf("txt=%s%n", ctx.getText());
      this.rewriter.replace(ctx.start, ctx.stop, ctx.getText());
    }
  }

}
