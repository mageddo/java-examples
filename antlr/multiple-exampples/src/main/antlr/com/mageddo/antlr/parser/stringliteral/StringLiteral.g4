grammar StringLiteral;

@header {
  package com.mageddo.antlr.parser.stringliteral;
}

json
   : STRING
   ;

STRING
   : '"' (ESC | SAFECODEPOINT)* '"'
   ;

fragment ESC
   : '\\' (["\\/bfnrt])
   ;

fragment SAFECODEPOINT
   : ~ ["\\\u0000-\u001F]
   ;

WS
   : [ \t\n\r] + -> skip
   ;
