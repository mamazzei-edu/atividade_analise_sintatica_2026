## O Problema da Recursão à Esquerda na Análise Descendente Recursiva

Antes de falar sobre a recursão, é preciso entender de onde surgem os seus problemas.

O problema da recursão é resultado de como funcionam as regras das nossas notações para descrever nossas linguagens de programação. Uma descrição gramatical BNF/EBNF é uma lista não ordenada de regras. As regras são usadas para definir símbolos com a ajuda de outros símbolos. Nós temos dois tipos de símbolos:

* **Terminal (ou símbolo de terminal)** : Terminais são sequências de caracteres escritas entre aspas. Devem ser usadas como estão. Nada está oculto por trás delas e não admitem mais nenhuma operação sobre o seu valor. Por exemplo, "Instituto", "avenida", "+", "if" e "id".
* **Símbolo não terminal (ou símbolo não terminal)** : Às vezes precisamos de um nome para nos referirmos a outra coisa. Esses são chamados de não terminais. Em BNF, os nomes de não terminais são escritos entre colchetes angulares (por exemplo, \<declaração>), enquanto em EBNF geralmente não usam colchetes (por exemplo: declaração,). Equivalem as variáveis da nossa linguagem e são utilizados para representam as transformações ou derivações que a nossa linguagem permite.  

[Esse arquivo](BnfEbnf.md) apresenta um quadro comparativo e um resumo de como funcionam as notações BNF e EBNF, se você precisar rever. 

Você deve se lembrar que cada regra na BNF e na EBNF possui três partes:

* **Lado esquerdo**: Aqui escrevemos um não terminal para defini-lo.
* **É definido como**: Há dois símbolos utilizados para representar essa parte. 
    * **::=** são os símbolos utilizados na BNF
    * **=** é o símbolo utilizado na EBNF
* **Lado direito**: Aqui vai a definição do não terminal especificado no lado esquerdo.

**O problema da recursão surge exatamente da forma como implementamos BNF e EBNF em código**

## Recursão à Esquerda: BNF vs EBNF em Parsers Descendentes Recursivos

### O problema na BNF

Em um parser descendente recursivo, cada não-terminal vira uma função. Uma regra com **recursão à esquerda** faz a função chamar a si mesma *antes* de consumir qualquer token, gerando loop infinito:

```bnf
<expr> ::= <expr> "+" <term>   ← recursão à esquerda: LOOP INFINITO
         | <term>
```

```
parse_expr()
  → chama parse_expr()         ← sem consumir nada
      → chama parse_expr()
          → chama parse_expr() ...  💥
```

Por isso, em BNF, a solução clássica é **reescrever com recursão à direita**:

```bnf
<expr>      ::= <term> <expr'>
<expr'>     ::= "+" <term> <expr'>   ← recursão à direita: OK
              | ε
```

Agora a função consome `<term>` antes de se chamar novamente, avançando sempre no input. Preste atenção também ao fato de que a <expr'> contém a recursão à direita ou pode estar vazia ( | ε )

---

### Na EBNF: o problema desaparece

**Sim, o problema de recursão à esquerda afetaria a EBNF da mesma forma *se você usasse regras recursivas*.** A EBNF não é imune à recursão à esquerda por natureza — ela é imune na *prática* porque seus operadores de repetição **eliminam a necessidade de recursão**.

A chave está no operador `{ }` (repetição), que é mapeado diretamente para um **laço iterativo**, não para uma chamada recursiva:

```ebnf
expr = term { "+" term } ;
```

A função gerada para essa regra seria:

```python
def parse_expr():
    parse_term()              # consome pelo menos um term
    while current_token == "+":  # laço, não recursão
        consume("+")
        parse_term()
```

Não há chamada recursiva alguma — logo, **não há risco de loop infinito**.

---

### Comparação direta

| Situação | BNF | EBNF |
|---|---|---|
| Recursão à esquerda | 💥 Loop infinito | 💥 Loop infinito (se usada) |
| Solução disponível | Reescrever com recursão à direita + `ε` | Usar `{ }` ou `[ ]` — iteração nativa |
| Código gerado | Funções recursivas | Laços iterativos |
| Legibilidade da solução | Baixa — regras auxiliares artificiais | Alta — intenção diretamente expressa |

---

### Por que a EBNF resolve estruturalmente

A equivalência formal entre os dois lados abaixo é a essência da solução:

```
BNF  →  <A> ::= <A> B | ε      (recursão à esquerda — proibida)
BNF  →  <A> ::= B <A> | ε      (recursão à direita — permitida, mas indireta)
EBNF →  A = { B } ;             (iteração — direta e segura)
```

Os três expressam a mesma linguagem (*zero ou mais ocorrências de B*), mas apenas a EBNF traduz isso em código sem envolver recursão. A **notação reflete o mecanismo de implementação**: `{ }` é literalmente um `while`, não uma chamada de função.

---

> **Conclusão:** O problema de recursão à esquerda é uma limitação dos *parsers descendentes recursivos*, não da notação em si. A EBNF não elimina o problema por ser "mais inteligente" — ela o *contorna estruturalmente*, pois seus operadores de repetição e opcionalidade são implementados como iteração, tornando desnecessário o uso de regras recursivas para expressar os padrões mais comuns de repetição.


## Exemplo Prático: Eliminando a Recursão à Esquerda 

**1. O Problema: A Regra em BNF (Recursiva à Esquerda)**
Na matemática, a adição é associativa à esquerda. Por isso, a forma mais clássica e natural de escrever a regra de uma expressão de soma em notação BNF pura é:

BNF
<expr> ::= <expr> '+' <term> 
         | <term>
Leitura: "Uma expressão é uma expressão somada a um termo, OU apenas um termo".

O que acontece no código do Analisador Descendente Recursivo?
Se tentarmos traduzir essa regra diretamente para o Java, criaremos um laço infinito fatal:

```Java
private void expr() throws Exception {
    // Para resolver o lado esquerdo da regra <expr> '+' <term>
    // o analisador chama a si mesmo ANTES de consumir qualquer token!
    expr(); 
    accept(Tag.PLUS);
    term();
}
```

Como o método expr() chama expr() na primeira linha, a pilha de execução (Call Stack) do Java vai encher até o programa travar com um erro de StackOverflowError.

**2. A Solução Matemática: Fatoração (BNF Pura)**

Para resolver isso matematicamente na BNF (criando uma recursão à direita), nós dividimos o problema criando um novo símbolo não-terminal (vamos chamá-lo de <expr_linha>), que representa "o resto da soma":

```BNF
<expr>       ::= <term> <expr_linha>
<expr_linha> ::= '+' <term> <expr_linha> 
               | vazio (ε)
```
Apesar de resolver o travamento, essa abordagem gera um código Java fragmentado, com muitos métodos auxiliares (exprLinha()) que deixam o programa complexo.

**3. A Solução Moderna e Elegante: Iteração com EBNF**

A metalinguagem EBNF introduziu o conceito de repetição (Fecho de Kleene), representado pelas chaves { }, que significam "repetido zero ou mais vezes". Com isso, podemos achatar a recursão transformando-a em uma iteração:

```EBNF
expr = term, { '+', term } ;
```

Leitura: "Uma expressão começa obrigatoriamente com um termo, seguido por zero ou infinitas repetições de um sinal de '+' e outro termo".

**Por que a EBNF é a melhor amiga do programador?**
Porque o símbolo de repetição { } da EBNF é traduzido diretamente para um laço while no código Java! A recursão perigosa desaparece, e a estrutura fica extremamente limpa:

```Java
private void expr() throws Exception {
    // 1. Lê o primeiro <term> obrigatoriamente
    term(); 
    
    // 2. O bloco { '+' term } vira um laço while!
    // Enquanto o token atual for um sinal de soma...
    while (token.tag == Tag.PLUS) {
        accept(Tag.PLUS); // Consome o '+'
        term();           // Lê o próximo termo
    }
}
```