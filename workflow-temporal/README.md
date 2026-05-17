# workflow-temporal

POC em Java mostrando como usar Temporal para orquestrar a criação assíncrona de uma carteira de exemplo durante o onboarding de um investidor no IWC.

## Problema resolvido

O onboarding precisa disparar etapas síncronas e assíncronas dependentes entre si:

- criar a wallet
- solicitar investimentos de exemplo
- solicitar `FinancialEventCandidate`
- esperar o processamento em background terminar
- concluir ou abortar a pipeline

Sem uma engine de workflow durável, esse fluxo costuma depender de polling frágil, estados espalhados e tratamento manual de retry e timeout.

## Por que Temporal é útil

Temporal permite:

- modelar a pipeline como workflow durável
- esperar sinais de processamento externo sem perder estado
- aplicar retry em activities
- controlar timeout de ponta a ponta
- coordenar fan-out/fan-in dos candidates

## Pipeline representada

```text
Investor onboarding
  -> SampleWalletCreationWorkflow
      -> createWallet
      -> createInvestments
      -> createFinancialEventCandidates
      -> processFinancialEventCandidates
      -> finishSampleWalletCreation
```

No projeto, `createInvestments` e `createFinancialEventCandidates` persistem a intenção de criação e um dispatcher em background executa o trabalho real. Quando cada candidate termina, o worker externo sinaliza o workflow Temporal, que faz o fan-in e só conclui quando todos forem processados.

## Como executar

O projeto usa:

- Java 25 via Gradle toolchain
- Gradle Wrapper
- Temporal Java SDK
- Quarkus + Hibernate ORM com PostgreSQL
- PostgreSQL embedded com Zonky nos testes

Execute:

```bash
./gradlew test
```

## Estrutura

- `workflow`: interface e implementação do workflow Temporal
- `activity`: contracts e implementação das activities
- `domain`: entidades e enums do onboarding
- `dataprovider`: interfaces `DAO` e implementações `DaoPg` com Quarkus/Hibernate
- `src/test`: testes automatizados com `TestWorkflowEnvironment`

## Evolução para produção

Em produção, a evolução natural seria:

- trocar o dispatcher local por filas/consumidores reais
- expor workers Temporal em processos separados
- usar migrations versionadas com Flyway ou Liquibase
- publicar métricas e tracing por etapa
- refinar políticas de retry por tipo de falha
- separar entidades JPA das entidades de domínio se a complexidade crescer
