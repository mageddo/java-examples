package com.mageddo.antlr.parser.comment;

import com.mageddo.antlr.parser.json.JSONLexer;

import com.mageddo.antlr.parser.json.JSONParser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Test;

import testing.TestUtils;
import static testing.TestUtils.getResourceAsString;

public class CommentTest {
  @Test
  void mustParseComment(){

    final var lexer = new CommentLexer(CharStreams.fromString(getResourceAsString(
        "/comment-test/scenario-01.txt"
    )));
    final var tokens = new CommonTokenStream(lexer);
    final var parser = new CommentParser(tokens);

    final var valueListener = new CommentBaseListener(){
      public void enterJson(CommentParser.JsonContext ctx) {
        System.out.printf("enterJson: %s%n", ctx.getText());
      }
    };
    ParseTreeWalker.DEFAULT.walk(valueListener, parser.json());

  }
}
