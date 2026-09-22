# Finalizar Pedido

> Exemplo ilustrativo com domínio fictício de loja. Substitua pelo comportamento real do seu
> projeto.

## Objetivo

Permitir que o cliente converta o `Carrinho` em um `Pedido` confirmado, com pagamento
autorizado, encerrando a jornada de compra e liberando o pedido para separação.

## Pontos de entrada

- `POST /checkout`: ação "Finalizar compra" na tela do carrinho.

## Pré-condições

- Existe um `Carrinho` com ao menos um item.
- O cliente está autenticado e tem um endereço de entrega selecionado.

## Jornada

1. O cliente revisa o `Carrinho` e aciona "Finalizar compra".
2. O sistema recalcula o total (itens + frete − descontos) e apresenta o resumo.
3. O cliente escolhe a forma de pagamento e confirma.
4. O sistema cria o `Pedido` em `AGUARDANDO_PAGAMENTO` e solicita a autorização ao
   `Pagamento`.
5. Com a autorização aprovada, o `Pedido` passa a `PAGO` e o `Carrinho` é esvaziado.
6. O cliente é levado à tela de confirmação com o número do pedido.

### Autorização recusada

Quando, no passo 5, a autorização é recusada, o `Pedido` passa a `PAGAMENTO_RECUSADO`, o
`Carrinho` é **preservado** e o cliente é convidado a tentar outra forma de pagamento.

## Resultado esperado

Ao final da jornada:

* existe um `Pedido` em `PAGO` vinculado ao cliente;
* o `Carrinho` correspondente está vazio;
* o total cobrado é igual ao resumo apresentado antes da confirmação.

## Regras de negócio

- O total do pedido é `soma dos itens + frete − descontos`, arredondado a 2 casas decimais.
- Um `Carrinho` vazio não pode originar um `Pedido`.
- A reserva de estoque acontece na criação do `Pedido`; a recusa de pagamento **libera** a
  reserva.
- Um mesmo `Carrinho` não gera dois pedidos `PAGO`: a finalização é idempotente por carrinho.

## Interações

### `Pedido`

Agregado que guarda itens, total e status. Decide as transições
`AGUARDANDO_PAGAMENTO → PAGO/PAGAMENTO_RECUSADO`; não decide autorização de crédito.

### `Pagamento`

Solicita a autorização ao provedor e devolve aprovado/recusado. Não altera estoque.

### `Carrinho`

Fonte dos itens; é esvaziado apenas quando o pedido chega a `PAGO`.

## Cenários de aceitação

### Pagamento aprovado confirma o pedido

Given um carrinho com itens e um cliente autenticado
When o cliente finaliza a compra e o pagamento é aprovado
Then é criado um pedido em `PAGO`
And o carrinho fica vazio

### Pagamento recusado preserva o carrinho

Given um carrinho com itens
When o cliente finaliza a compra e o pagamento é recusado
Then o pedido fica em `PAGAMENTO_RECUSADO`
And o carrinho é preservado para nova tentativa

## Fora do escopo

- Cálculo detalhado do frete, coberto por outra feature de logística.
- Política de limite de itens do carrinho, coberta pela spec
  [`2026-01-15-limite-itens-carrinho`](../../superpowers/specs/2026-01-15-limite-itens-carrinho.md).

## Conceitos de domínio relacionados

- [Pedido](../../model/domain-model.md)
- [Carrinho](../../model/domain-model.md)
- [Pagamento](../../model/domain-model.md)
