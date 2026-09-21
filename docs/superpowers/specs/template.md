# <Título da mudança>

- Status: DRAFT/TODO/DONE/CANCELED
- Data: YYYY-MM-DD

> Este arquivo é um template. Remova esta orientação e todas as seções que não agregarem valor à Spec final.
> Prefira decisões explícitas, contratos verificáveis e exemplos concretos. A Spec deve registrar o que precisa ser verdadeiro, não antecipar em detalhe como o código será implementado.

## Objetivo

Descreva em poucos parágrafos o resultado que a mudança deve produzir, o problema que ela resolve e as restrições mais importantes.

## Contexto

Explique apenas o contexto necessário para entender a decisão. Registre o comportamento atual, limitações relevantes e por que a mudança é necessária.

Remova esta seção quando o objetivo já fornecer contexto suficiente.

## Escopo

- <comportamento, componente ou contrato que faz parte da mudança>;
- <outro item explicitamente incluído>.

## Decisões e regras

Descreva as decisões funcionais e técnicas que precisam permanecer verdadeiras durante a implementação.

Use subseções específicas do domínio em vez de manter este título genérico quando isso deixar a Spec mais clara. Exemplos:

- `## Semântica de concorrência e consistência`
- `## Estados e transições`
- `## APIs de consulta`
- `## Persistência e particionamento`
- `## Experiência da tela`
- `## Estratégia de correção`

Quando houver contrato tabular, prefira tabela:

| Campo | Decisão |
|---|---|
| `<campo>` | <decisão> |

Quando houver fluxo relevante, descreva a sequência explicitamente:

1. <entrada ou pré-condição>;
2. <validação ou decisão>;
3. <efeito esperado>;
4. <resultado exposto ao consumidor>.

## Implementação técnica

Use esta seção somente quando houver uma decisão técnica importante que precise ser preservada para evitar ambiguidade de arquitetura ou contrato.

Mantenha-a mínima. Prefira registrar:

- fronteiras e responsabilidades relevantes;
- contratos públicos que precisam ser alterados ou preservados;
- decisões de arquitetura, persistência, integração, concorrência ou idempotência quando forem parte essencial da solução;
- restrições técnicas que eliminem alternativas incompatíveis com a decisão aprovada.

Não transforme a Spec em plano de implementação. Em geral, evite:

- listar classe por classe ou arquivo por arquivo que deverá ser alterado;
- indicar linhas, métodos privados ou detalhes mecânicos de refatoração;
- prescrever trechos de código ou a sequência exata de implementação;
- antecipar decisões que podem ser tomadas durante a implementação sem alterar o comportamento ou a arquitetura aprovados.

Inclua exemplos de código, nomes concretos de classes, métodos, endpoints, eventos, tabelas ou schemas somente quando forem parte relevante do contrato ou quando forem necessários para tornar a decisão inequívoca.

Remova esta seção quando as decisões anteriores já forem suficientes para orientar a implementação.

## Testes

Defina o que precisa ser comprovado por testes e em qual nível, sem transformar a seção em um roteiro detalhado de implementação dos testes.

- testes unitários: <regras e estados que exigem cobertura>;
- component tests: <integrações que exigem infraestrutura real ou persistência>;
- fixtures/análise estática: <quando aplicável>;
- testes existentes: <o que deve ser preservado ou ajustado>.

Registre explicitamente quando **não** devem ser criados novos testes e por quê.

Remova níveis de teste que não se aplicarem.

## Fora do escopo

- <mudança relacionada que não será feita nesta entrega>;
- <alternativa ou evolução deliberadamente adiada>.

## Validação

Após a implementação:

1. <verificação estrutural ou funcional específica>;
2. <revisão de contratos, migrations, endpoints, mensagens ou estados>;
3. <revisão conforme as skills aplicáveis ao diff>;
4. executar, sem skips ou redução de escopo:

```shell
./gradlew build compTest
```

Todos os testes, compilações, Checkstyle, PMD e regras ArchUnit aplicáveis devem passar.

Ajuste o comando somente quando a Spec pertencer a um módulo com contrato de build diferente.

## Critérios de aceite

- <resultado observável e verificável>;
- <regra funcional que deve permanecer verdadeira>;
- <contrato técnico ou de integração que precisa ser atendido>;
- <condição de erro, consistência ou compatibilidade relevante>;
- o comando de validação definido nesta Spec passa integralmente.
