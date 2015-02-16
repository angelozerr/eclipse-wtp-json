package com.sun.jersey.json.impl.reader;
%%
%class JsonLexer
%unicode
%line
%char
%type JsonToken
%{
   StringBuffer string = new StringBuffer();
   public int getCharOffset() { return yychar; }
   public int getLineNumber() { return yyline; }
   public int getColumn() { return yycolumn; }
%}
%state STRING
WHITE_SPACE_CHAR=[\n\r\ \t\b\012]
NUMBER_TEXT=-?(0|[1-9][0-9]*)(\.[0-9]+)?([eE][+-]?[0-9]+)?
%%
<YYINITIAL> {
  "," { return (new JsonToken(JsonToken.COMMA, yytext(), yyline, yychar, yychar+1)); }
  ":" { return (new JsonToken(JsonToken.COLON, yytext(), yyline, yychar, yychar+1)); }
  "[" { return (new JsonToken(JsonToken.START_ARRAY, yytext(), yyline, yychar, yychar+1)); }
  "]" { return (new JsonToken(JsonToken.END_ARRAY, yytext(), yyline, yychar, yychar+1)); }
  "{" { return (new JsonToken(JsonToken.START_OBJECT, yytext(), yyline, yychar, yychar+1)); }
  "}" { return (new JsonToken(JsonToken.END_OBJECT, yytext(), yyline, yychar, yychar+1)); }
  "true" { return (new JsonToken(JsonToken.TRUE, yytext(), yyline, yychar, yychar+yylength()));}
  "false" { return (new JsonToken(JsonToken.FALSE, yytext(), yyline, yychar, yychar+yylength()));}
  "null" { return (new JsonToken(JsonToken.NULL, yytext(), yyline, yychar, yychar+yylength()));}
  \"  { string.setLength(0); yybegin(STRING); }
  {NUMBER_TEXT} { return (new JsonToken(JsonToken.NUMBER, yytext(), yyline, yychar, yychar+yylength()));} 
  {WHITE_SPACE_CHAR} {}
}

<STRING> {
 \"  { yybegin(YYINITIAL); 
       return (new JsonToken(JsonToken.STRING, string.toString(), yyline, yychar, yychar+string.length()));}
 [^\n\r\"\\]+   { string.append(yytext()); }
  \\\"          { string.append('\"'); }
  \\\\          { string.append('\\'); }
  \\\/           { string.append('/'); }
  \\b          { string.append('\b'); }
  \\f          { string.append('\f'); }
  \\n           { string.append('\n'); }
  \\r           { string.append('\r'); }
  \\t          { string.append('\t'); }
  \\u[0-9A-Fa-f]{4}    { string.append(Character.toChars(Integer.parseInt(yytext().substring(2),16))); }
}