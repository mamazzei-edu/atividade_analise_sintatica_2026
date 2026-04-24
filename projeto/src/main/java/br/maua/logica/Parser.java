package br.maua.logica;


public class Parser {
    private final Lexer lexer;
    private Token token;

    public Parser(Lexer lexer) throws Exception {
        this.lexer = lexer;
        this.token = lexer.yylex();
    }

    private void error(String esperado) throws Exception {
        throw new Exception("Erro Sintático: Esperava " + esperado + 
                            " mas recebeu " + token.tag + " ('" + token.lexeme + "') " +
                            "na linha " + token.line + ", coluna " + token.column);
    }

    // Função vital: verifica a marca atual e avança na entrada
    private void accept(Tag tag) throws Exception {
        if (token.tag == tag) {
            token = lexer.yylex(); // Avança para o próximo token
        } else {
            error(tag.toString());
        }
    }

    // REGRA: goal = prop ;
    public void parse() throws Exception {
        prop();
        // Se a expressão acabou, o próximo token obrigatoriamente deve ser o fim do arquivo
        if (token.tag == Tag.EOF) {
            System.out.println("Análise sintática terminada com sucesso! Expressão válida.");
        } else {
            error("Tag.EOF (Fim de expressão)");
        }
    }

    // =========================================================================
    // TODO: IMPLEMENTE AS REGRAS ABAIXO TRADUZINDO A EBNF PARA JAVA
    // =========================================================================

    // REGRA: prop = prop_exp, {'implies', prop_exp} ;
    private void prop() throws Exception {
        prop_exp();
        while (token.tag == Tag.IMPLIES) {
            accept(Tag.IMPLIES);
            prop_exp();
        }
    }

    // REGRA: prop_exp = prop_term, {'or', prop_term} ;
    private void prop_exp() throws Exception {
        /* TODO: Implementar */
    }

    // REGRA: prop_term = prop_factor, {'and', prop_factor} ;
    private void prop_term() throws Exception {
         /* TODO: Implementar */
    }

    // REGRA: prop_factor = {'not'}, prop_element ;
    private void prop_factor() throws Exception {
         /* TODO: Implementar */
    }

    // REGRA: prop_element = '(' prop ')' | prop_letter | prop_constant ;
    private void prop_element() throws Exception {
         /* TODO: Implementar (Dica: Use switch(token.tag) ou if/else) */
    }

}