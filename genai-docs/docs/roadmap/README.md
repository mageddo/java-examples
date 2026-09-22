# Roadmap

Visão **prospectiva** de evolução: para onde o produto vai e em que fases macro.

Se a pergunta é *"que fases fazem sentido para evoluir este produto?"* ou *"em que ordem
construímos o que falta?"*, a resposta está aqui.

- **Não** descreve o comportamento atual — isso é papel de [`../features`](../features/).
- **Não** descreve conceitos do domínio — isso é papel de [`../model`](../model/).
- **Não** é o "porquê" de uma mudança já feita — isso é papel de
  [`../superpowers/specs`](../superpowers/specs/).
- **Não** é o plano de implementação de uma mudança — isso é papel de
  [`../superpowers/plans`](../superpowers/plans/).

## Objetivo e motivação

O roadmap é a visão **macro e orientada a produto**: fatias verticais (cada uma entrega valor
ponta a ponta), dependências entre elas e o racional de sequenciamento. Ele amarra futuras
specs/plans em fases. Cada fase, ao entrar em execução, se desdobra em `superpowers/specs`
(o porquê), `superpowers/plans` (o como) e atualizações em `features` (o novo "hoje").

## Convenções

- Prefixo de data `AAAA-MM-DD-` no nome do arquivo.
- Escrever no futuro/condicional, diferente de `features` (presente).
- Referenciar `features` e `model` como base do que já existe, sem duplicar definição.

## Como escrever

1. Copie [`template.md`](template.md) para `AAAA-MM-DD-<titulo>.md`.
2. Defina fases como fatias verticais, com objetivo, escopo, dependências e valor entregue.

## Exemplo

- [`2026-01-20-fatiamento-checkout.md`](2026-01-20-fatiamento-checkout.md) — fatiamento
  fictício da evolução do checkout em fases.
