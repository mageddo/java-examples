# <Título do fatiamento>

Visão macro que organiza a evolução de <produto/jornada> em fases entregáveis. As fases são
**fatias verticais** (cada uma entrega valor ponta a ponta) e respeitam as dependências do
domínio.

Referências:

- Comportamento atual: [`../features`](../features/).
- Conceitos do domínio: [`../model`](../model/).

## Premissa que guia o fatiamento

Explique o critério de fatiamento: o que já existe, o que é fachada/provisório e por que a
ordem proposta destrava valor com segurança.

## Visão geral das fases

| Fase | Nome | Objetivo macro | Features cobertas |
|------|------|----------------|-------------------|
| **F0** | <nome> | <objetivo> | <features> |
| **F1** | <nome> | <objetivo> | <features> |

## Sequenciamento e dependências

```
F0 ──▶ F1 ──▶ F2
            └──▶ F3 (paralelizável)
```

- **F0 → F1**: <por que esta ordem>.

---

## F0 — <Nome>

**Objetivo.** <o resultado macro da fase>.

**Escopo.**

- <item>;
- <item>.

**Fica de fora.** <o que fica para fases posteriores>.

**Depende de.** <fases anteriores>.

**Valor entregue.** <valor de negócio da fatia>.

---

## F1 — <Nome>

...

---

## Como cada fase se desdobra

Ao entrar em execução, cada fase gera:

1. `superpowers/specs/AAAA-MM-DD-*.md` — o porquê de cada mudança;
2. `superpowers/plans/AAAA-MM-DD-*.md` — o como (plano de implementação);
3. atualização das `features/*` — o novo "hoje";
4. quando aplicável, ajuste do `model`.
