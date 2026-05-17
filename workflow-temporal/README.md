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
- Flyway para schema versionado
- PostgreSQL embedded com Zonky nos testes

Execute:

```bash
./gradlew test
```

## Ambiente de desenvolvimento

Suba a infraestrutura local:

```bash
docker compose up postgres-17 temporal-db-bootstrap
docker compose up temporal temporal-ui
./gradlew quarkusDev
```

Endpoints úteis em desenvolvimento:

- aplicação Quarkus: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/q/swagger-ui`
- Temporal UI: `http://localhost:8233`

JMX disponível via HTTP:

```bash
curl -X POST "http://localhost:8080/jmx/sample-wallets/investor-1?timeoutSeconds=120"
```

O projeto usa o mesmo PostgreSQL do `docker-compose` para:

- domínio `com.mageddo.investment_product` no schema `inv`
- estado principal do Temporal no schema `temporal`
- visibility store do Temporal no schema `temporal_visibility`

O schema `temporal_visibility` existe porque o Temporal mantém metadata de versionamento própria para o store de visibility. A separação continua no mesmo datasource PostgreSQL, mas evita colisão entre tabelas internas do servidor.

## Estrutura

- `workflow`: interface e implementação do workflow Temporal
- `activity`: contracts e implementação das activities
- `domain`: entidades e enums do onboarding
- `dataprovider`: interfaces `DAO` e implementações `DaoPg` com Quarkus/Hibernate
- `entrypoint`: JMX HTTPizado para disparar o workflow
- `src/main/resources/db/migration`: migrations versionadas do schema
- `src/test`: testes automatizados com `TestWorkflowEnvironment`

## Evolução para produção

Em produção, a evolução natural seria:

- trocar o dispatcher local por filas/consumidores reais
- expor workers Temporal em processos separados
- evoluir o conjunto de migrations Flyway conforme o domínio crescer
- publicar métricas e tracing por etapa
- refinar políticas de retry por tipo de falha
- separar entidades JPA das entidades de domínio se a complexidade crescer


## Namespace

No projeto atual, o namespace do client está vindo de sample-wallet.temporal.namespace em src/main/resources/application.properties:1 e é aplicado em src/main/java/com/mageddo/temporal/
sample_wallet/TemporalClientProvider.java:1.

Basta trocar:

sample-wallet.temporal.namespace=default

por:

sample-wallet.temporal.namespace=investment_product

Mas isso sozinho não basta. O namespace também precisa existir no servidor Temporal. Registre assim:

docker-compose exec temporal temporal operator namespace create \
--namespace investment_product \
--retention 24h \
--description "Investment Product namespace"

Depois reinicie a app Quarkus para o WorkflowClient pegar o novo valor.

Se quiser validar:

docker-compose exec temporal temporal operator namespace describe --namespace investment_product

Se preferir, eu também posso ajustar o docker-compose para já criar esse namespace automaticamente no bootstrap, em vez de depender de comando manual.

