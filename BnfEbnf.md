# BNF vs EBNF — Quadro Comparativo

## Componentes e Notações

| Conceito | BNF | EBNF (ISO 14977) | Correspondência |
|---|---|---|---|
| **Definição de regra** | `<símbolo> ::= expressão` | `símbolo = expressão ;` | ⚠️ Equivalente, mas sintaxe diferente — `::=` vs `=`, sem `< >`, ponto-e-vírgula obrigatório no fim |
| **Alternativa (ou)** | `A \| B` | `A \| B` | ✅ Idêntico |
| **Concatenação** | `A B` (justaposição) | `A , B` | ⚠️ Equivalente — EBNF exige vírgula explícita |
| **Agrupamento** | ❌ Não possui | `( A \| B )` | 🆕 Exclusivo da EBNF |
| **Opcional (0 ou 1×)** | `<A> ::= B \| ε` (workaround) | `[ A ]` | 🆕 EBNF possui notação direta; BNF requer regra auxiliar com vazio |
| **Repetição (0 ou mais×)** | Requer recursão: `<A> ::= <A> B \| ε` | `{ A }` | 🆕 EBNF possui notação direta; BNF usa regra recursiva |
| **Repetição (1 ou mais×)** | Requer recursão: `<A> ::= B \| <A> B` | `A , { A }` ou extensão `A+` | 🆕 EBNF resolve com composição; BNF usa recursão |
| **Símbolo não-terminal** | `<nome>` (entre `< >`) | `nome` (sem delimitadores) | ⚠️ Equivalente — apenas convenção de marcação diferente |
| **Símbolo terminal** | `"texto"` ou sem marcação | `"texto"` ou `'texto'` | ✅ Praticamente idêntico — EBNF permite aspas simples ou duplas |
| **String vazia (ε / vazio)** | `ε` ou `<vazio>` | `""` ou omissão | ⚠️ Equivalente — representações distintas |
| **Comentário** | ❌ Não definido formalmente | `(* comentário *)` | 🆕 Exclusivo da EBNF |
| **Exceção** | ❌ Não possui | `A - B` (A exceto B) | 🆕 Exclusivo da EBNF |
| **Terminação de regra** | Implícita (quebra de linha) | `;` (obrigatório) | ⚠️ Diferente — EBNF é explícita |

---

## Legenda

| Ícone | Significado |
|---|---|
| ✅ | Notações idênticas ou quase idênticas nas duas linguagens |
| ⚠️ | Conceito equivalente, mas com sintaxe diferente |
| 🆕 | Recurso presente **apenas na EBNF** — não existe em BNF puro |

---

## Exemplo Comparativo

Regra para um **número inteiro não-vazio** (dígito repetido um ou mais vezes):

**BNF** — exige recursão explícita:
```bnf
<dígito>  ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
<inteiro> ::= <dígito> | <inteiro> <dígito>
```

**EBNF** — notação direta e compacta:
```ebnf
dígito  = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" ;
inteiro = dígito , { dígito } ;
```

---

> **Conclusão:** A EBNF é um superconjunto da BNF. Tudo que pode ser expresso em BNF pode ser expresso em EBNF, mas a recíproca não é direta — construções EBNF como `[ ]`, `{ }`, `( )` e `-` precisam ser "desdobradas" em múltiplas regras recursivas para serem representadas em BNF puro.


