# Modelo de Domínio — Loja (exemplo)

> Exemplo ilustrativo com domínio fictício de loja. Substitua pelo modelo real do seu projeto.

O domínio cobre a compra de produtos por um `Cliente`: da montagem de um `Carrinho` até a
confirmação de um `Pedido` com `Pagamento` autorizado.

## `Cliente`

Pessoa que compra na loja. Possui identificação e endereços de entrega. Não decide preços nem
disponibilidade de estoque; apenas origina `Carrinho` e `Pedido`.

## `Carrinho`

Coleção mutável de itens (produto + quantidade) de um `Cliente` antes da compra. É efêmero:
existe para ser convertido em `Pedido`. Um `Carrinho` vazio não pode originar um `Pedido`.

### Invariantes

- A quantidade de cada item é um inteiro positivo.
- O `Carrinho` pertence a exatamente um `Cliente`.

## `Pedido`

Agregado que representa uma compra confirmada. Fixa os itens, o total e o status no momento da
finalização — é um snapshot imutável dos itens (mudanças posteriores no catálogo não o
alteram).

### Estados de `Pedido`

- `AGUARDANDO_PAGAMENTO`: criado, aguardando autorização.
- `PAGO`: pagamento autorizado; liberado para separação.
- `PAGAMENTO_RECUSADO`: autorização negada; estoque liberado.
- `CANCELADO`: encerrado sem entrega.

### Ciclo de vida

- `AGUARDANDO_PAGAMENTO → PAGO`: autorização aprovada; esvazia o `Carrinho` de origem.
- `AGUARDANDO_PAGAMENTO → PAGAMENTO_RECUSADO`: autorização negada; libera a reserva de
  estoque e preserva o `Carrinho`.
- `PAGO → CANCELADO`: cancelamento com estorno do `Pagamento`.

### Invariantes

- `total = soma dos itens + frete − descontos`, arredondado a 2 casas decimais.
- Um `Carrinho` gera no máximo um `Pedido` em `PAGO` (finalização idempotente por carrinho).

## `Pagamento`

Representa a tentativa de cobrança de um `Pedido` junto a um provedor externo. Devolve
aprovado ou recusado; não altera estoque nem status do pedido diretamente — apenas informa o
resultado que o `Pedido` usa para transicionar.

### Estados de `Pagamento`

- `PENDENTE`, `AUTORIZADO`, `RECUSADO`, `ESTORNADO`.

## Glossário

| Termo | Definição |
|---|---|
| `Reserva de estoque` | bloqueio de unidades no momento da criação do `Pedido` |
| `Snapshot de itens` | cópia imutável de itens/preços fixada no `Pedido` |
