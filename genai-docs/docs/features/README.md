# Features

Documentação **canônica de comportamento**: como o sistema funciona hoje.

Se a pergunta é *"como funciona o cancelamento?"*, *"o que acontece quando o pagamento
falha?"* ou *"por onde o usuário entra nesse fluxo?"*, a resposta está aqui.

- Conceitos do domínio (o que é `Pedido`, o que é `Carrinho`) ficam em [`../model`](../model/).
- Por que o comportamento é esse hoje fica em [`../superpowers/specs`](../superpowers/specs/).
- O registro na íntegra das reuniões que originaram o produto fica em
  [`../discussions`](../discussions/).

## Objetivo e motivação

A feature é a **fonte da verdade sobre o comportamento atual**. Ela existe para que qualquer
pessoa entenda o sistema **sem depender de conversas, prompts ou decisões não registradas**.
Descreve o presente e o observável, não a implementação interna nem o histórico.

## Como escrever

1. Leia [`conventions.md`](conventions.md).
2. Copie [`template.md`](template.md) para `<produto>/<jornada>.md`.
3. Escreva no presente, descrevendo o sistema como ele é.

## Exemplo

- [`loja/finalizar-pedido.md`](loja/finalizar-pedido.md) — jornada fictícia de finalização de
  um pedido no checkout, ilustrando o formato de uma feature.
