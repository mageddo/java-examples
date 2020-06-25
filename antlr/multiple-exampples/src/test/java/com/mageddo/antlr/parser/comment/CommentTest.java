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

    final var content = getResourceAsString("/comment-test/scenario-01.txt");
    System.out.println(content);
    final var lexer = new CommentLexer(CharStreams.fromString(content));
    final var tokens = new CommonTokenStream(lexer);
    final var parser = new CommentParser(tokens);

    final var valueListener = new CommentBaseListener(){
      public void enterComment(CommentParser.CommentContext ctx) {
        System.out.println("comment: " + ctx.getText());
//        System.out.println(ctx.SINGLE_LINE_COMMENT().getText());
      }
    };
    ParseTreeWalker.DEFAULT.walk(valueListener, parser.base());

  }
}
