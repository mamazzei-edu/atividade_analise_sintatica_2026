package br.maua.matematica;

import br.maua.matematica.Scanner;

// classe para o analisador sintático
public class Parser {
    // referência a um scanner criado separadamente pelo JFlex
    private final Scanner scanner;
    // token atualmente obtido pelo Scanner
    private Token token;

    // construtor da classe Parser
    public Parser(Scanner scanner) throws Exception {
        // se não tem um scanner, então não é possível continuar
        if (scanner == null) {
            throw new Exception("Scanner não definido! Não foi possível criar o Parser!");
        }
        // armazena a referência
        this.scanner = scanner;
        // e obtém o primeiro token
        token = scanner.yylex();
    }

    // função para tratar erros
    private void error(Tag tag) throws Exception {
        String msg = "Erro Sintático: durante a análise sintática esperava marca com tag " + tag +
                " mas recebi marca com tag " + token.tag + "\n" +
                "Linha: " + token.line + " e Coluna: " + token.column;
        System.out.println(msg);
        throw new Exception(msg);
    }

    private void error(Tag[] tags) throws Exception {
        String msg = "Erro Sintático: durante a análise sintática esperava marcas com tags [ ";
        for (Tag t : tags) {
            msg += t;
            msg += ' ';
        }
        msg += ']';
        msg += " mas recebi marca com tag " + token.tag + "\n" +
                "Linha: " + token.line + " e Coluna: " + token.column;
        System.out.println(msg);
        throw new Exception(msg);
    }

    // função que verifica a marca atual e avança na entrada
    private void accept(Tag tag) throws Exception {
        // Se o token atual é o que se está esperando
        // Imprime o token atual antes de avançar, para facilitar o acompanhamento do processo de análise sintática
        System.out.println("Validando token após o token: " + token.tag + " ('" + token.lexeme + "')");
        if (token.tag == tag) {
            // avança um token na entrada
            token = scanner.yylex();
        } else {
            // gera uma exceção
            error(tag);
        }
        System.out.println("Token validado: " + token.tag + " ('" + token.lexeme + "')");

    }

    // função que inicia a análise sintática descendente
    // recursiva
    public void parse() throws Exception {
        // Imprime o token inicial para ajudar na compreensão do processo de análise sintática
        System.out.println("=====================================================================================");
        System.out.println("Começando com token inicial: " + token.tag + " ('" + token.lexeme + "')");
        // REGRA: goal = expr;
        expr();
        // se, depois de expr() o token for EOF, tudo deu certo!
        if (token.tag == Tag.EOF) {
            System.out.println("Análise sintática terminada com sucesso!");
            System.out.println("=====================================================================================");
        } else {
            error(Tag.EOF);
            System.out.println("=====================================================================================");
        }
    }

    // REGRA: expr = term, { ('+' | '-'), term };
    private void expr() throws Exception {
        term();
        while (token.tag == Tag.PLUS || token.tag == Tag.MINUS) {
            accept(token.tag);
            term();
        }
    }

    // REGRA: term = factor, { ( '*' | '/' | '%'), factor };
    private void term() throws Exception {
        factor();
        while (token.tag == Tag.TIMES ||
                token.tag == Tag.DIV ||
                token.tag == Tag.MOD) {
            accept(token.tag);
            factor();
        }
    }

    // REGRA: factor = '(', expr, ')' | number | id;
    private void factor() throws Exception {
        switch (token.tag) {
            case LPAREN:
                accept(Tag.LPAREN);
                expr();
                accept(Tag.RPAREN);
                break;
            case NUMBER:
                accept(Tag.NUMBER);
                break;
            case ID:
                accept(Tag.ID);
                break;
            default:
                error(new Tag[] { Tag.LPAREN, Tag.NUMBER, Tag.ID });
                break;
        }
    }
}