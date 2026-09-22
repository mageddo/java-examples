# Fatiamento da Evolução do Checkout

> Exemplo ilustrativo com domínio fictício de loja. Substitua por roadmaps reais do seu projeto.

Visão macro que organiza a evolução do checkout em fases entregáveis. As fases são **fatias
verticais** (cada uma entrega valor ponta a ponta) e respeitam as dependências do domínio
(`Carrinho → Pedido → Pagamento`).

Referências:

- Comportamento atual: [`../features/loja/finalizar-pedido.md`](../features/loja/finalizar-pedido.md).
- Conceitos do domínio: [`../model/domain-model.md`](../model/domain-model.md).

## Premissa que guia o fatiamento

O checkout já finaliza pedidos com pagamento à vista, mas ainda não trata parcelamento, nem
resiliência a falhas do provedor, nem visão operacional. O fatiamento prioriza primeiro a
robustez do fluxo existente e só depois novas formas de pagamento e observabilidade.

## Visão geral das fases

| Fase | Nome | Objetivo macro | Features cobertas |
|------|------|----------------|-------------------|
| **F0** | Robustez do checkout | Tornar a finalização resiliente e idempotente | finalizar-pedido |
| **F1** | Formas de pagamento | Suportar parcelamento e novos métodos | finalizar-pedido (variações) |
| **F2** | Operação & Observabilidade | Painel de pedidos, métricas de conversão e falha | (novas features) |

## Sequenciamento e dependências

```
F0 ──▶ F1 ──▶ F2
```

- **F0 → F1**: só faz sentido diversificar formas de pagamento sobre um fluxo idempotente e
  resiliente.
- **F1 → F2**: a visão operacional ganha valor quando há volume e variedade de pagamentos.

---

## F0 — Robustez do checkout

**Objetivo.** Garantir que a finalização seja idempotente e resiliente a falhas transitórias
do provedor de pagamento.

**Escopo.**

- Idempotência por carrinho na finalização;
- Retentativa controlada em falha transitória de autorização;
- Liberação correta da reserva de estoque em recusa.

**Fica de fora.** Parcelamento e novos métodos de pagamento.

**Valor entregue.** Menos pedidos inconsistentes e menos reservas de estoque presas.

---

## F1 — Formas de pagamento

**Objetivo.** Ampliar as opções de pagamento além do à vista.

**Escopo.**

- Parcelamento com regras de juros;
- Novos métodos (ex.: carteira digital), como variações da feature de finalização.

**Fica de fora.** Antifraude e análise de risco.

**Depende de.** F0.

**Valor entregue.** Aumento de conversão por oferecer mais opções ao cliente.

---

## F2 — Operação & Observabilidade

**Objetivo.** Dar visibilidade do funil de checkout ao time de operação.

**Escopo.**

- Painel de pedidos por status;
- Métricas de conversão e de recusa de pagamento.

**Depende de.** F0 e F1 (volume e variedade para medir).

**Valor entregue.** Decisões operacionais baseadas em dados reais do checkout.

---

## Como cada fase se desdobra

Ao entrar em execução, cada fase gera:

1. `superpowers/specs/AAAA-MM-DD-*.md` — o porquê de cada mudança;
2. `superpowers/plans/AAAA-MM-DD-*.md` — o como;
3. atualização das `features/loja/*` — o novo "hoje";
4. quando aplicável, ajuste do `model/domain-model.md`.
