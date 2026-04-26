## O Problema da Recursão à Esquerda na Análise Descendente Recursiva

Antes de falar sobre a recursão, é preciso entender de onde surgem os seus problemas.

O problema da recursão é resultado de como funcionam as regras das nossas notações para descrever nossas linguagens de programação. Uma descrição gramatical BNF/EBNF é uma lista não ordenada de regras. As regras são usadas para definir símbolos com a ajuda de outros símbolos. Nós temos dois tipos de símbolos:

* **Terminal (ou símbolo terminal):** Terminais são sequências de caracteres muitas vezes escritas entre aspas. Devem ser usadas como estão. Nada está oculto por trás delas e não admitem mais nenhuma operação sobre o seu valor. Por exemplo: `"Instituto"`, `"avenida"`, `"+"`, `"if"` e `"id"`.
* **Não Terminal (ou variável sintática):** Às vezes precisamos de um nome para nos referirmos a outra coisa. Esses são chamados de não terminais. Em BNF, os nomes de não terminais são escritos entre colchetes angulares (por exemplo, `<declaracao>`), enquanto em EBNF geralmente não usam colchetes (por exemplo: `declaracao`). Equivalem **às** variáveis da nossa linguagem e são utilizados para **representar** as transformações ou derivações que a nossa gramática permite.  

[Esse arquivo](BnfEbnf.md) apresenta um quadro comparativo e um resumo de como funcionam as notações BNF e EBNF, caso você precise rever. 

Você deve se lembrar que cada regra na BNF e na EBNF possui três partes:

1. **Lado esquerdo:** Aqui escrevemos um não terminal para defini-lo.
2. **É definido como:** Símbolo de atribuição.
    * `::=` é o símbolo utilizado na BNF.
    * `=` é o símbolo utilizado na EBNF.
3. **Lado direito:** Aqui vai a definição do não terminal especificado no lado esquerdo.

**O problema da recursão surge exatamente da forma como implementamos a BNF e a EBNF em código!**

---

## Recursão à Esquerda: O Desafio em Parsers Descendentes Recursivos

Em um analisador sintático (parser) descendente recursivo (Top-Down), cada não terminal da gramática vira uma função no código (ex: no Java). 

Na matemática, operações como a adição são associativas à esquerda. Por isso, a forma mais clássica e natural de escrever a regra de uma expressão de soma em notação BNF pura é utilizando a **recursão à esquerda**:

```bnf
<expr> ::= <expr> "+" <term>   ← recursão à esquerda: LOOP INFINITO
         | <term>
```
*Leitura: "Uma expressão é uma expressão somada a um termo, OU apenas um termo".*

### O Problema na Prática

Se tentarmos traduzir essa regra BNF diretamente para o Java, a função chamará a si mesma *antes* de consumir qualquer token do arquivo, criando um laço infinito fatal:

```java
private void expr() throws Exception {
    // Para tentar resolver o lado esquerdo da regra <expr> '+' <term>
    // o analisador chama a si mesmo ANTES de ler o sinal de '+'!
    expr(); 
    accept(Tag.PLUS);
    term();
}
```

O que acontece na memória (Call Stack)?
```text
expr()
  → chama expr()         ← sem consumir nenhum token!
      → chama expr()
          → chama expr() ...  💥 StackOverflowError!
```

### A Solução Matemática: Fatoração (BNF Pura)

Para resolver isso mantendo a BNF pura, a solução clássica é reescrever a regra invertendo a lógica para uma **recursão à direita**. Nós dividimos o problema criando um novo símbolo (vamos chamá-lo de `<expr_linha>`), que representa "o resto da soma":

```bnf
<expr>       ::= <term> <expr_linha>
<expr_linha> ::= "+" <term> <expr_linha>   ← recursão à direita: OK
               | ε  (vazio)
```

Agora a função consome o `<term>` *antes* de se chamar novamente, avançando sempre no texto lido. Apesar de resolver o travamento, essa abordagem gera um código Java fragmentado, com muitos métodos auxiliares (ex: `exprLinha()`) que deixam o programa complexo e artificial.

---

## Na EBNF: A Solução Moderna e Elegante

A EBNF não é imune à recursão à esquerda por mágica. Ela resolve o problema estruturalmente porque seus operadores de repetição **eliminam a necessidade de escrever regras recursivas**.

A metalinguagem EBNF introduziu o conceito de repetição (Fecho de Kleene), representado pelas chaves `{ }`, que significam "repetido zero ou mais vezes". Com isso, podemos achatar a recursão transformando-a em uma **iteração**:

```ebnf
expr = term, { "+", term } ;
```
*Leitura: "Uma expressão começa obrigatoriamente com um termo, seguido por zero ou infinitas repetições de um sinal de '+' e outro termo".*

**Por que a EBNF é a melhor amiga do programador?**
A notação reflete o mecanismo de implementação: o símbolo `{ }` é literalmente traduzido para um laço `while` no código, e não para uma chamada de função! A recursão perigosa desaparece, não há risco de loop infinito e a estrutura fica extremamente limpa:

```java
private void expr() throws Exception {
    // 1. Lê o primeiro <term> obrigatoriamente
    term(); 
    
    // 2. O bloco { "+" term } vira um laço iterativo (while)!
    while (token.tag == Tag.PLUS) {
        accept(Tag.PLUS); // Consome o '+'
        term();           // Lê o próximo termo
    }
}
```

---

### Comparação Direta

| Característica | BNF | EBNF |
|---|---|---|
| **Recursão à esquerda** | 💥 Loop infinito | 💥 Loop infinito (se usada) |
| **Solução disponível** | Reescrever com recursão à direita + `ε` | Usar `{ }` ou `[ ]` (iteração nativa) |
| **Código gerado** | Funções recursivas fragmentadas | Laços iterativos simples (`while`) |
| **Legibilidade da solução** | Baixa — regras auxiliares artificiais | Alta — intenção diretamente expressa |

> **Conclusão:** O problema de recursão à esquerda é uma limitação dos *parsers descendentes recursivos*, não da notação em si. A EBNF *contorna estruturalmente* esse problema, pois seus operadores de repetição são implementados como iteração na linguagem de programação (Java, Python, C), tornando desnecessário o uso de funções recursivas perigosas para expressar os padrões comuns da linguagem.