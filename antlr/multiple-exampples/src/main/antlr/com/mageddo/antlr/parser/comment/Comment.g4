grammar Comment;

@header {
  package com.mageddo.antlr.parser.comment;
}

base
   : value
   ;

value
  : comment NL (comment)*
  ;

comment
  : SINGLE_LINE_COMMENT
  ;

SINGLE_LINE_COMMENT
   : '#' (LINE_TEXT)+
   ;

NL
  : '\r'? '\n'
  ;

fragment LINE_TEXT
   : ~ [\r\n] // what which isn't a new line
   ;
