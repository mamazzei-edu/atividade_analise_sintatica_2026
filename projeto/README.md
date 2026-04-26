# Atividade: Modelo e projeto de Analisador Sintático Descendente Recursivo

## Objetivo
Até agora, nosso compilador consegue apenas "ler" palavras (Análise Léxica). 
O próximo passo é entender se essas palavras formam frases que fazem sentido. Nesta atividade preparatória, você construirá manualmente um **Analisador Sintático Descendente Recursivo** para validar Fórmulas Bem Formadas (FBF) da Lógica Proposicional. Para isso, você tem um modelo de como funciona um Analisador Sintático Descendente Recursivo no modelo.

Os analisadores top-down (ex: descida recursiva, como o nosso) não conseguem lidar com recursão à esquerda (Left Recursion).

Se você não se lembra do que é Recursão à Esquerda e de quais problemas ela pode gerar, faça essa [Revisao de Recursão em notações](../RevisaoRecursaoEsquerda)

## A Gramática
Abaixo está a gramática (em notação EBNF) que rege a nossa Lógica Proposicional:

```ebnf
goal          = prop ;
prop          = prop_exp, {'implies', prop_exp} ;
prop_exp      = prop_term, {'or', prop_term} ;
prop_term     = prop_factor, {'and', prop_factor} ;
prop_factor   = {'not'}, prop_element ;
prop_element  = '(', prop, ')' | prop_letter | prop_constant ;
prop_letter   = 'A' | 'B' | ... | 'Z' ;
prop_constant = 'true' | 'false' ;
```

## O Desafio

O Analisador Léxico (JFlex) já foi escrito e configurado para você no projeto Maven que se encontra na pasta **projeto**. Ele reconhecerá palavras como implies, and, A, true, etc., e devolverá os respectivos Tokens.

Sua missão é abrir a classe Parser.java e implementar os métodos correspondentes a cada regra da gramática EBNF.

Como estamos baseando nosso Parser na EBNF a nossa implementação irá garantir que a recursão à esquerda não entre em loops infinitos, desde que você faça corretamente o consumo dos tokens de entrada e a implementação da gramática em EBNF.

## Dicas de Implementação:

Para cada Não-Terminal da EBNF (ex: prop_exp), crie um método privado no Java (ex: private void prop_exp() throws Exception).

Se a EBNF diz para ler outro Não-Terminal, chame o método dele.

Se a EBNF diz para ler uma palavra específica (Terminal), use a função accept(Tag.NOME_DA_TAG).

Se a EBNF tem uma repetição { }, use um laço while (token.tag == Tag.NOME_DA_TAG) { ... } no Java.

Se a EBNF tem opções |, use if/else ou switch analisando a tag do token atual (token.tag).


## Como testar

Se você quiser ver o modelo funcionando, dentro da pasta **modelo** execute o seguinte comando:

mvn test

Agora que vocâ já viu como funciona o modelo, dentro da pasta **projeto**, execute o comando Maven abaixo no seu terminal para rodar o avaliador automático:

mvn test

Se a sua lógica de descida recursiva estiver correta, todos os testes de lógica proposicional executarão corretamente e no github classroom ficarão verdes!
