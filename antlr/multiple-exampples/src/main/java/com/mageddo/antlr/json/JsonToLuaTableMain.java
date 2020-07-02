package com.mageddo.antlr.json;

import java.io.IOException;

import com.mageddo.antlr.parser.json.JSONBaseListener;
import com.mageddo.antlr.parser.json.JSONLexer;

import com.mageddo.antlr.parser.json.JSONParser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

public class JsonToLuaTableMain {
  public static void main(String... args) throws IOException {
    final var lexer = new JSONLexer(CharStreams.fromStream(
        JsonToLuaTableMain.class.getResourceAsStream("/json/example01.json")
    ));
    final var tokens = new CommonTokenStream(lexer);
    final var parser = new JSONParser(tokens);

    final var jsonToLuaTableConverter = new JsonToLuaTableConverter(tokens);
    ParseTreeWalker.DEFAULT.walk(jsonToLuaTableConverter, parser.json());

    System.out.println(jsonToLuaTableConverter.getText());
  }

  static class JsonToLuaTableConverter extends JSONBaseListener {

    final TokenStreamRewriter streamRewriter;

    JsonToLuaTableConverter(TokenStream tokens) {
      this.streamRewriter = new TokenStreamRewriter(tokens);
    }

    @Override
    public void enterPair(JSONParser.PairContext ctx) {
      ctx
          .getTokens(JSONLexer.T__3)
          .forEach(it -> {
            final var accept = it.accept(new AbstractParseTreeVisitor<String>() {
              public String visitTerminal(TerminalNode node) {
                return "=";
              }
            });
            this.streamRewriter.replace(it.getSymbol(), accept);
          });
      ctx
          .getTokens(JSONLexer.STRING)
          .forEach(it -> {
            final var accept = it.accept(new AbstractParseTreeVisitor<String>() {
              public String visitTerminal(TerminalNode node) {
//                System.out.println("string terminal: " + node.getText());
                return node.getText().substring(1, node.getText().length() - 1);
              }
            });
            this.streamRewriter.replace(it.getSymbol(), accept);
          });
//      System.out.println("entered pair: " + ctx.getText());
//      System.out.println("tokens: " + ctx.getTokens(JSONLexer.T__3));
    }

    @Override
    public void enterArr(JSONParser.ArrContext ctx) {
      // you can use ParseTreeVisitor as well
      this.streamRewriter.insertAfter(ctx.getStart(), "{");
      this.streamRewriter.delete(ctx.getStart());
      this.streamRewriter.insertAfter(ctx.getStop(), "}");
      this.streamRewriter.delete(ctx.getStop());
    }

    public String getText() {
      return this.streamRewriter.getText();
    }
  }
}
