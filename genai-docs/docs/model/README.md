# Model (Modelo de Domínio)

Definição **canônica dos conceitos** do domínio: o que é cada entidade, agregado, estado e
invariante — o vocabulário compartilhado pelo time.

Se a pergunta é *"o que é um `Pedido`?"*, *"quais estados um `Pagamento` pode ter?"* ou
*"qual a diferença entre `Carrinho` e `Pedido`?"*, a resposta está aqui.

- **Como** o sistema se comporta hoje fica em [`../features`](../features/).
- **Por que** uma regra é assim fica em [`../superpowers/specs`](../superpowers/specs/).

## Objetivo e motivação

O modelo existe para dar **um único vocabulário**. Features e specs referenciam esses
conceitos em `code style` (`Pedido`, `PAGO`) em vez de redefini-los, evitando divergência de
significado entre documentos. Descreve conceito e invariante, não jornada nem tela.

## Como escrever

1. Copie [`template.md`](template.md) para `domain-model.md` (ou um arquivo por agregado).
2. Descreva cada conceito no presente: o que é, seus estados, suas relações e invariantes.
3. Não descreva jornadas ou telas — isso é papel de `features`.

## Exemplo

- [`domain-model.md`](domain-model.md) — modelo fictício de loja (`Pedido`, `Carrinho`,
  `Pagamento`, `Cliente`), ilustrando o formato.
