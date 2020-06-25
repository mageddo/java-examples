package com.mageddo.antlr.parser.stringliteral;

import com.mageddo.antlr.parser.comment.CommentBaseListener;
import com.mageddo.antlr.parser.comment.CommentLexer;
import com.mageddo.antlr.parser.comment.CommentParser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static testing.TestUtils.getResourceAsString;

public class StringLiteralTest {
  @Test
  void mustParseStringLiteral(){

    final var lexer = new CommentLexer(CharStreams.fromString(getResourceAsString(
        "/stringliteral-test/scenario-01.txt"
    )));
    final var tokens = new CommonTokenStream(lexer);
    final var parser = new CommentParser(tokens);

    final var valueListener = new CommentBaseListener(){
      public void enterJson(CommentParser.JsonContext ctx) {
        System.out.printf("enterJson: %s%n", ctx.getText());
        assertEquals("\"some text\"", ctx.getText());
      }
    };
    ParseTreeWalker.DEFAULT.walk(valueListener, parser.json());

  }
}
