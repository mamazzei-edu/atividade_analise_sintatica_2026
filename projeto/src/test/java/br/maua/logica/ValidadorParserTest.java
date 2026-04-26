package br.maua.logica;


import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;

public class ValidadorParserTest {

    private void validarFBF(String expressao) throws Exception {
        Lexer lexer = new Lexer(new StringReader(expressao));
        Parser parser = new Parser(lexer);
        parser.parse();
    }

    @Test
    public void test01_LetrasEConstantesSimples() throws Exception {
        validarFBF("A");
        validarFBF("true");
        validarFBF("false");
        assertTrue(true); // Se não lançar exceção, o teste passa
    }

    @Test
    public void test02_OperadoresLogicosBasicos() throws Exception {
        validarFBF("A and B");
        validarFBF("true or false");
        validarFBF("not A");
        validarFBF("A implies B");
        assertTrue(true);
    }

    @Test
    public void test03_AssociatividadeERepeticao() throws Exception {
        // Testa os blocos { } da EBNF
        validarFBF("A and B and C and D");
        validarFBF("A or B or true");
        validarFBF("not not not A");
        assertTrue(true);
    }

    @Test
    public void test04_FormulasBemFormadasComplexas() throws Exception {
        // Expressão complexa com parênteses e múltiplos operadores
        validarFBF("(A implies B) and (not C or true) implies (Z and false)");
        assertTrue(true);
    }

    @Test
    public void test05_ErroSintaticoDeveriaFalhar() {
        try {
            validarFBF("A and or B"); // Dois operadores seguidos (Erro)
            fail("O parser deveria ter recusado a expressão (Erro Sintático).");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Erro Sintático"));
        }
    }
    
    @Test
    public void test06_ErroSintaticoParentesesNaoFechado() {
        try {
            validarFBF("(A and B"); // Esqueceu de fechar parenteses
            fail("O parser deveria ter apontado a falta do parêntese fechado.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Erro Sintático"));
        }
    }
}