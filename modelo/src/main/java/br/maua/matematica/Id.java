package br.maua.matematica;

public class Id extends Token {

    public Id(String s, int line, int column, String lexeme) {
        super(Tag.ID, line, column, lexeme);
    }

    @Override
    public String toString() {
        return "<" + this.tag + ",\"" + this.lexeme + "\">";
    }

}
