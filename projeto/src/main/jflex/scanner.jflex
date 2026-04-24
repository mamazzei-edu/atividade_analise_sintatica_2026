package br.maua.logica;


%%

%public
%class Lexer
%unicode
%type Token
%line
%column
%yylexthrow Exception

%eofval{
    return new Token(Tag.EOF, yyline, yycolumn, "");
%eofval}

WhiteSpace = [ \t\n\r]+
Letter     = [A-Z]

%%

{WhiteSpace}    { /* ignorar espaços em branco */ }
"implies"       { return new Token(Tag.IMPLIES, yyline, yycolumn, yytext()); }
"or"            { return new Token(Tag.OR, yyline, yycolumn, yytext()); }
"and"           { return new Token(Tag.AND, yyline, yycolumn, yytext()); }
"not"           { return new Token(Tag.NOT, yyline, yycolumn, yytext()); }
"true"          { return new Token(Tag.TRUE, yyline, yycolumn, yytext()); }
"false"         { return new Token(Tag.FALSE, yyline, yycolumn, yytext()); }
"("             { return new Token(Tag.LPAREN, yyline, yycolumn, yytext()); }
")"             { return new Token(Tag.RPAREN, yyline, yycolumn, yytext()); }
{Letter}        { return new Token(Tag.LETTER, yyline, yycolumn, yytext()); }
.               { throw new Exception("Lexer Erro: símbolo ilegal " + yytext()); }
