package br.maua.logica;

public class Token {
    public final Tag tag;
    public final int line;
    public final int column;
    public final String lexeme;

    public Token(Tag tag, int line, int column, String lexeme) {
        this.tag = tag;
        this.line = line;
        this.column = column;
        this.lexeme = lexeme;
    }
}
