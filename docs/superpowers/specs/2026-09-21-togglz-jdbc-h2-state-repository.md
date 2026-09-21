# Migrar o state repository do Togglz de in-memory para JDBC (H2 em memória)

- Status: TODO
- Data: 2026-09-21

## Objetivo

Trocar o `StateRepository` do Togglz no módulo `feature-toggle-togglz-spring` de `InMemoryStateRepository` para `JDBCStateRepository`, persistindo o estado das features em um banco H2 em memória embarcado na própria aplicação, mantendo todo o comportamento observável do exemplo (enum de features, API `isActive`/`getValue`/`setValue`, `FeatureJmx` e console Togglz).

Hoje o estado das features vive apenas em memória no `FeatureManager`, sem passar por uma camada de persistência. O objetivo é demonstrar o Togglz operando sobre um repositório JDBC real, ainda que o backing store seja um H2 em memória.

## Contexto

Estrutura atual do módulo:

- `Main` — `@SpringBootApplication` que registra manualmente um bean `FeatureManager` com `featureEnum(FeatureSwitch.class)`, `InMemoryStateRepository` e `NoOpUserProvider`.
- `FeatureSwitch` — enum de features (`MY_FIRST_JOB` com `@EnabledByDefault`, `FEATURE_TWO` desabilitada) com API `isActive`/`getValue`/`setValue`.
- `FeatureJmx` — `@RestController` (rotas `/jmx/features/**`) que consulta e altera features.
- `FeatureController` — `@RestController` expondo `/features/my-first-job`.
- `application.properties` — habilita o console Togglz em `/actuator/features` e o Swagger UI.
- `MainCompTest` — component test (`@SpringBootTest`) cobrindo estados default e alternância de feature via `FeatureManager`.

Restrições relevantes:

- `JDBCStateRepository` (Togglz 4.x) requer um `javax.sql.`/`jakarta` `DataSource` e cria/gerencia sua própria tabela de estado.
- O bean `FeatureManager` manual em `Main` tem precedência sobre a auto-config do `togglz-spring-boot-starter`; a troca do repositório é feita nele.
- O `@EnabledByDefault` do Togglz é aplicado como fallback quando não há estado persistido para a feature; assim, os defaults continuam valendo sem seed.

## Escopo

- Adicionar as dependências necessárias para prover um `DataSource` H2 (`spring-boot-starter-jdbc` + `com.h2database:h2`);
- Configurar um `DataSource` H2 em memória via `application.properties`;
- Trocar, no bean `FeatureManager` de `Main`, o `InMemoryStateRepository` por `JDBCStateRepository` construído sobre o `DataSource` injetado;
- Preservar o comportamento do exemplo (features, defaults, JMX e console Togglz);
- Estender o `MainCompTest` para comprovar a persistência via JDBC;
- Validar build e execução local.

## Decisões e regras

### Dependências

| Dependência | Decisão |
|---|---|
| `org.springframework.boot:spring-boot-starter-jdbc` | Adicionada — provê o `DataSource` autoconfigurado |
| `com.h2database:h2` | Adicionada — driver/engine do banco em memória |
| `org.togglz:togglz-spring-boot-starter` / `togglz-console` | Mantidas em `4.6.4` |

### DataSource H2 (`application.properties`)

| Item | Decisão |
|---|---|
| URL | `jdbc:h2:mem:togglz;DB_CLOSE_DELAY=-1` — banco em memória que sobrevive enquanto a aplicação estiver de pé |
| Driver / credenciais | Padrão H2 (autoconfig do Spring Boot) |
| H2 web console | Não exposto (mantém o exemplo focado no Togglz) |

### Wiring (`Main`)

- O bean `featureManager` passa a receber o `DataSource` por injeção (parâmetro do método `@Bean`).
- Substituir `new InMemoryStateRepository()` por `JDBCStateRepository.newBuilder(dataSource).build()`, que cria a tabela de estado automaticamente.
- Mantidos `featureEnum(FeatureSwitch.class)` e `NoOpUserProvider`.

### Estado default

- Sem seed explícito: o `@EnabledByDefault` do Togglz garante `MY_FIRST_JOB` ativa e `FEATURE_TWO` inativa quando não há estado persistido na tabela.

### Comportamento a preservar

- A API pública de `FeatureSwitch` (`isActive`, `getValue`, `setValue`), o `FeatureController`, o `FeatureJmx` e o console Togglz em `/actuator/features` permanecem com o mesmo contrato observável.
- `MY_FIRST_JOB` ativa por padrão; `FEATURE_TWO` inativa por padrão.

## Testes

- component test (`MainCompTest`, `@SpringBootTest`): além de manter a cobertura dos estados default e da alternância de feature via `FeatureManager`, é estendido para comprovar a **persistência JDBC** — após alterar o estado de uma feature, um novo `JDBCStateRepository` construído sobre o mesmo `DataSource` deve reler o estado a partir da tabela, provando que a mudança foi persistida no banco e não apenas em memória de processo do `FeatureManager`.
- unitários: não se aplicam de forma relevante — a lógica é delegada ao `FeatureManager`/`JDBCStateRepository` do Togglz; mockar o framework produziria testes artificiais.
- fixtures/Templates: não necessários — o cenário é o próprio enum de features, sem massa de dados a montar.

## Fora do escopo

- Persistência durável (H2 file-based, Postgres, etc.) — o backing store permanece em memória;
- Exposição do H2 web console;
- Seed/inicialização explícita de estados via SQL;
- Migração para a auto-config do `togglz-spring-boot-starter` (o bean manual em `Main` é mantido);
- Reestruturação em camadas DDD — incompatível com a natureza de exemplo mínimo do módulo.

## Validação

Após a implementação:

1. `./gradlew build` compila e executa o component test com sucesso;
2. Subir a aplicação localmente (`./gradlew bootRun`) e confirmar boot sem erros e o console Togglz respondendo em `/actuator/features`;
3. Revisão do diff conforme as skills `principal-dev` e `test-dev`.

```shell
./gradlew build
```

> Observação: este módulo standalone não expõe a task `compTest`; os component tests rodam dentro de `build`.

## Critérios de aceite

- Dependências `spring-boot-starter-jdbc` e `h2` adicionadas;
- `DataSource` H2 em memória configurado (`jdbc:h2:mem:togglz;DB_CLOSE_DELAY=-1`);
- O bean `FeatureManager` usa `JDBCStateRepository` sobre o `DataSource` injetado, com a tabela de estado criada automaticamente;
- Exemplo Togglz preservado: `MY_FIRST_JOB` ativa por padrão, `FEATURE_TWO` inativa, toggle via `FeatureSwitch`/`FeatureJmx` funcionando, console em `/actuator/features` acessível;
- O component test comprova que o estado alterado é persistido e relido via JDBC;
- `./gradlew build` passa integralmente.
