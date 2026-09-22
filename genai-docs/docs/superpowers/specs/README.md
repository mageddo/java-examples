# Specs

O **porquê formalizado** de cada mudança: qual problema ela resolve, o que precisa ser
verdadeiro e quais decisões devem ser preservadas durante a implementação.

Se a pergunta é *"por que passamos a limitar o carrinho?"* ou *"o que exatamente esta
mudança deve garantir?"*, a resposta está aqui.

- **Como** o sistema funciona hoje fica em [`../../features`](../../features/).
- **Como** implementar tarefa a tarefa fica em [`../plans`](../plans/).
- O registro bruto que originou a mudança fica em [`../../discussions`](../../discussions/).

## Objetivo e motivação

A spec registra **o que precisa ser verdade**, não como o código será escrito. Ela é o
contrato verificável de uma mudança: objetivo, escopo, decisões/regras, testes e critérios de
aceite. Uma spec não está concluída enquanto a `feature` correspondente não refletir o novo
comportamento.

## Como escrever

1. Copie [`template.md`](template.md) para `AAAA-MM-DD-<titulo>.md`.
2. Prefira decisões explícitas, contratos verificáveis e exemplos concretos.
3. Não transforme a spec em plano de implementação — isso é papel de [`../plans`](../plans/).

## Exemplo

- [`2026-01-15-limite-itens-carrinho.md`](2026-01-15-limite-itens-carrinho.md) — spec
  fictícia que formaliza o limite de itens no carrinho.
