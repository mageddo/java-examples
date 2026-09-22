# Convenções das features

## Natureza do documento

- A feature é a **fonte da verdade sobre o comportamento atual** da aplicação.
- Ela descreve o presente. Não narra decisão, alternativa descartada, migração ou o que o
  sistema fazia antes — isso é papel de `docs/superpowers/specs`.
- Não usar tempo futuro nem linguagem de proposta ("vamos", "será alterado", "passará a").
- Um comportamento removido é **apagado** da feature, não marcado como obsoleto. O registro
  de que ele existiu já está na spec e no histórico do git.
- O documento deve ser compreensível sem depender de conversas, prompts ou decisões não
  registradas no repositório.

## Nomes e organização

- Um arquivo por jornada, em `docs/features/<produto>/<jornada>.md`.
- Nome em kebab-case, sem prefixo de data e sem repetir o produto que o diretório já carrega:
  `loja/finalizar-pedido.md`, não `loja/2026-01-15-loja-finalizar-pedido.md`.
- O nome deve ser adivinhável por quem procura a funcionalidade sem conhecer o repositório.
- Variações de uma mesma jornada que mudam o comportamento de forma relevante ganham arquivo
  próprio (`finalizar-pedido-pagamento-a-vista.md`, `finalizar-pedido-pagamento-parcelado.md`);
  variações que só mudam um passo ficam como seção dentro do arquivo da jornada.

## Estrutura

Seguir [`template.md`](template.md). Seções sem conteúdo real são removidas, não preenchidas
com placeholder.

| Seção | Conteúdo |
|---|---|
| `Objetivo` | para que serve e qual necessidade de negócio atende |
| `Pontos de entrada` | por onde o usuário ou o sistema inicia a jornada |
| `Pré-condições` | estado que precisa existir antes |
| `Jornada` | passos observáveis, numerados, no presente |
| `Resultado esperado` | pós-condições observáveis ao final da jornada |
| `Regras de negócio` | invariantes e regras que valem sempre, independente do passo |
| `Interações` | quais agregados e módulos participam e o que cada um faz |
| `Cenários de aceitação` | Given/When/Then dos casos que validam a feature |
| `Fora do escopo` | comportamentos vizinhos que esta feature deliberadamente não cobre |
| `Conceitos de domínio relacionados` | links para `docs/model` |

## Escrita

- Descrever comportamento observável, não a implementação interna.
- Usar o vocabulário do domínio, em `code style`, quando se referir a um conceito
  (`Pedido`, `Carrinho`, `Pagamento`, `PAGO`).
- Informar explicitamente valores, datas, arredondamentos, tolerâncias e restrições
  relevantes.
- Não usar expressões vagas como "validar corretamente", "funcionar normalmente" ou "etc."
  quando elas representarem uma regra.
- Given/When/Then é a linguagem dos cenários de aceitação, sem runtime Cucumber.

## Manutenção

- A feature é atualizada **na mesma mudança** que altera o comportamento, não depois.
- Uma spec que altera comportamento não está concluída enquanto a feature correspondente não
  refletir o novo comportamento.
- Se a feature e o código divergem, o bug pode estar em qualquer um dos dois: a divergência
  é investigada e resolvida, nunca silenciada ajustando a feature ao que o código faz.
