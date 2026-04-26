package br.maua.matematica;

public class Number extends Token {

    public final double value;

    public Number(double v, int line, int column, String lexeme) {
        super(Tag.NUMBER, line, column, lexeme);
        value = v;
    }

    @Override
    public String toString() {
        return "<" + this.tag + "," + this.value + ">";
    }
}
