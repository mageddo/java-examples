# Reunião — Limite de itens no carrinho

- Data: 2026-01-10
- Origem: notas da reunião de produto (fictícias)
- Natureza: resumo executivo
- Participantes: Produto, Engenharia, Atendimento

> Exemplo ilustrativo. Substitua por registros reais do seu projeto.

## Contexto

O time de Atendimento reportou carrinhos com centenas de unidades do mesmo item, gerando
falhas na reserva de estoque e cobranças inconsistentes. A reunião discutiu se e como limitar
a quantidade por carrinho.

## Registro

- Atendimento: "Na semana passada tivemos 3 casos de carrinho com mais de 500 unidades; o
  estoque reserva, o pagamento falha e o cliente reclama."
- Engenharia: "A reserva é feita na criação do pedido; sem limite, um carrinho gigante trava
  o fluxo. Um teto por item e por carrinho resolveria a maioria dos casos."
- Produto: "Concordo com um teto. Precisa ser configurável? Por ora, um valor fixo resolve.
  Podemos revisitar depois com dados."
- Engenharia: "Sugiro 100 unidades por item e 500 no carrinho como ponto de partida."
- Produto: "Fechado para começar. Mensagem clara para o cliente ao atingir o limite."

## Deliberações

- Adotar limite de **100 unidades por item** e **500 unidades no carrinho** (valores iniciais).
- Exibir mensagem explicativa ao cliente ao atingir o limite, sem bloquear o resto do
  carrinho.
- Encaminhamento: abrir spec formalizando a regra (ver
  [`2026-01-15-limite-itens-carrinho`](../superpowers/specs/2026-01-15-limite-itens-carrinho.md)).

## Pendências / Próximos passos

- Avaliar, com dados futuros, se o limite deve ser configurável por categoria de produto.
