# Limite de itens no carrinho

- Status: DONE
- Data: 2026-01-15

> Exemplo ilustrativo com domínio fictício de loja. Substitua por specs reais do seu projeto.

## Objetivo

Impedir que um `Carrinho` acumule quantidades excessivas que quebram a reserva de estoque e
geram cobranças inconsistentes na finalização. A mudança introduz um teto de quantidade por
item e por carrinho, com mensagem clara ao cliente.

## Contexto

Hoje o `Carrinho` aceita qualquer quantidade. Carrinhos com centenas de unidades do mesmo
item causaram falhas na reserva de estoque e recusas de pagamento (ver
[`discussions/2026-01-10-reuniao-limite-carrinho`](../../discussions/2026-01-10-reuniao-limite-carrinho.md)).
Não há, hoje, nenhuma validação de teto.

## Escopo

- Validação de quantidade máxima por item ao adicionar/atualizar um item do `Carrinho`;
- Validação de quantidade máxima total do `Carrinho`;
- Mensagem de feedback ao cliente ao atingir o limite.

## Decisões e regras

| Regra | Decisão |
|---|---|
| Máximo por item | `100` unidades |
| Máximo por carrinho | `500` unidades (soma de todos os itens) |
| Ação ao exceder | rejeitar a operação que excede, preservando o restante do carrinho |
| Mensagem | informar o limite atingido e a quantidade máxima permitida |

Fluxo ao adicionar/atualizar item:

1. cliente adiciona/atualiza a quantidade de um item;
2. valida-se `quantidade do item <= 100`;
3. valida-se `soma das quantidades <= 500`;
4. se qualquer validação falha, a operação é rejeitada com mensagem e o carrinho permanece
   no estado anterior.

## Testes

- testes unitários: teto por item (99/100/101), teto por carrinho (limites de borda), e
  preservação do carrinho ao rejeitar;
- testes existentes: manter verdes os testes de finalização de pedido.

## Fora do escopo

- Limite configurável por categoria de produto (adiado; revisitar com dados).
- Alteração no cálculo de frete ou desconto.

## Validação

Após a implementação:

1. adicionar item com quantidade 101 é rejeitado com mensagem;
2. carrinho que ultrapassaria 500 unidades rejeita apenas a operação excedente;
3. o comando de build/testes do projeto passa integralmente.

## Critérios de aceite

- Nenhum item do carrinho excede 100 unidades;
- O total do carrinho nunca excede 500 unidades;
- Ao atingir o limite, o cliente recebe mensagem clara e o carrinho é preservado;
- A feature [`finalizar-pedido`](../../features/loja/finalizar-pedido.md) permanece válida.
