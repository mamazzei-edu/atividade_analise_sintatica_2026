package br.maua.matematica;

import org.junit.Test;

import br.maua.matematica.Parser;
import br.maua.matematica.Scanner;

import static org.junit.Assert.*;
import java.io.StringReader;

public class ValidadorParserTest {

    private void validarExpressaoMatematica(String expressao) throws Exception {
        Scanner scanner = new Scanner(new StringReader(expressao));
        Parser parser = new Parser(scanner);
        parser.parse();
    }

    @Test
    public void test01ExpssoesSimples() throws Exception {
        validarExpressaoMatematica("b");
        validarExpressaoMatematica("1");
        validarExpressaoMatematica("30");
        validarExpressaoMatematica("a");
        assertTrue(true); // Se não lançar exceção, o teste passa
    }

    @Test
    public void test02_OperadoresMatematicos() throws Exception {
        validarExpressaoMatematica("1 + 2");
        validarExpressaoMatematica("a * b");
        validarExpressaoMatematica("2 * 3");
        validarExpressaoMatematica("5 / 2");
        assertTrue(true);
    }

    @Test
    public void test03_OperadoresMatematicosComParenteses() throws Exception {
        // Testa os blocos { } da EBNF
        validarExpressaoMatematica("(1 + 2) * 3");
        validarExpressaoMatematica("((1 + 2) * 3) / 4");
        assertTrue(true);
    }

    @Test
    public void test04_FormulaMatematicaComplexa() throws Exception {
        // Expressão complexa com parênteses e múltiplos operadores
        validarExpressaoMatematica("(1 + 2) * (3 - 4) / 5 + 6  * (7 + 8)");
        validarExpressaoMatematica("(21 + 2 * 3) - 4 / 5 + 6  * (7 + 8)");
        assertTrue(true);
    }

    @Test
    public void test05_ErroSintaticoDeveriaFalhar() {
        try {
            validarExpressaoMatematica("1 + + 2"); // Dois operadores seguidos (Erro)
            fail("O parser deveria ter recusado a expressão (Erro Sintático).");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Erro Sintático"));
        }
    }
    
    @Test
    public void test06_ErroSintaticoParentesesNaoFechado() {
        try {
            validarExpressaoMatematica("(1 + 2"); // Esqueceu de fechar parenteses
            fail("O parser deveria ter apontado a falta do parêntese fechado.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Erro Sintático"));
        }
    }
}