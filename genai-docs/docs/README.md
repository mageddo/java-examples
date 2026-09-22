# Documentação do Projeto

Este diretório é um **template de estrutura de documentação** para projetos de software.
Ele organiza a documentação por **papel**: cada pasta responde a um tipo de pergunta
diferente e tem uma fronteira explícita do que **não** é sua responsabilidade.

> Os exemplos deste template usam um domínio fictício de **loja/checkout** (`Pedido`,
> `Carrinho`, `Pagamento`), apenas para ilustrar o formato. Substitua-os pelo domínio real do
> seu projeto.

## Motivação

Documentação costuma apodrecer porque mistura tudo num lugar só: o que o sistema faz hoje, o
porquê de uma decisão antiga, o registro bruto de uma reunião e o plano do que ainda será
feito. Ao separar por papel, cada documento tem uma fonte da verdade clara, um tempo verbal
próprio e um critério objetivo de atualização.

## Mapa das pastas

| Pasta | Pergunta que responde | Tempo verbal | Fonte da verdade de |
|---|---|---|---|
| [`features/`](features/) | *"Como o sistema funciona hoje?"* | presente | comportamento atual |
| [`model/`](model/) | *"O que é este conceito de domínio?"* | presente | conceitos e invariantes do domínio |
| [`discussions/`](discussions/) | *"O que foi dito/decidido na reunião?"* | passado | memória bruta, sem síntese |
| [`superpowers/specs/`](superpowers/specs/) | *"Por que esta mudança foi feita e o que precisa ser verdade?"* | presente/imperativo | o porquê formalizado de cada mudança |
| [`superpowers/plans/`](superpowers/plans/) | *"Como implementar esta mudança, tarefa a tarefa?"* | imperativo | plano de execução de uma mudança |
| [`roadmap/`](roadmap/) | *"Para onde o produto vai e em que fases?"* | futuro/condicional | visão prospectiva de evolução |

## Como se relacionam

```
discussions ──▶ specs ──▶ plans ──▶ (código) ──▶ features
   (bruto)      (porquê)   (como)                  (hoje)

model  = vocabulário compartilhado, referenciado por features e specs
roadmap = visão macro que amarra futuras specs/plans em fases
```

- Uma **discussão** origina uma ou mais **specs**.
- Uma **spec** (o porquê) gera um **plan** (o como) e, ao ser implementada, atualiza as
  **features** (o novo "hoje") — na **mesma mudança**, nunca depois.
- O **model** é o dicionário de conceitos que features e specs referenciam em `code style`.
- O **roadmap** é a visão macro; cada fase se desdobra em specs/plans/features.

## Como usar este template

1. Copie a pasta `docs/` para o seu projeto.
2. Leia o README e as convenções de cada pasta.
3. Comece pelo `model/` (vocabulário) e pelas `features/` (comportamento atual).
4. A cada mudança de comportamento: escreva a `spec`, o `plan`, implemente e **atualize a
   feature na mesma entrega**.
5. Remova os exemplos fictícios de loja/checkout e os placeholders.
