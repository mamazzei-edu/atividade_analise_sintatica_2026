package br.maua.matematica;


%%

%public
%class Scanner
%unicode
%type Token
%line
%column
%yylexthrow Exception

%eofval{
    return new Token(Tag.EOF, yyline, yycolumn, "");
%eofval}

%line
%column


delim   = [\ \t\n]
ws      = {delim}+
digit	= [0-9]
number	= {digit}+(\.{digit}+)?([Ee][+-]?{digit}+)?
letter  = [A-Za-z]
id      = {letter}({letter}|{digit})*
%%

{ws}		{ /* ignorar */ }
{number}	{ return new Number(Double.parseDouble(yytext()),
                                    yyline, yycolumn, yytext()); }
{id}            {return new Id(yytext(), yyline, yycolumn, yytext()); }
"+"             {return new Token(Tag.PLUS, yyline, yycolumn, yytext());}
"-"             {return new Token(Tag.MINUS, yyline, yycolumn, yytext());}
"*"             {return new Token(Tag.TIMES, yyline, yycolumn, yytext());}
"/"             {return new Token(Tag.DIV, yyline, yycolumn,yytext());}
"("             {return new Token(Tag.LPAREN, yyline, yycolumn, yytext());}
")"             {return new Token(Tag.RPAREN, yyline, yycolumn, yytext());}
"%"             {return new Token(Tag.MOD, yyline, yycolumn ,yytext());}
.		        { throw new Exception("Scanner: símbolo ilegal "  +
                    "(\"" + yycharat(0) + "\")" + 
                    " na linha " + yyline + ", coluna " + yycolumn); }
