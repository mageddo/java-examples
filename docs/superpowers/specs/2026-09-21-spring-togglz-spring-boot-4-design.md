# Migração do módulo spring-togglz para Spring Boot 4

- Status: TODO
- Data: 2026-09-21

## Objetivo

Deixar o módulo de exemplo `spring-togglz` funcionando com ferramental e dependências atuais: Gradle wrapper próprio na última release stable, Spring Boot 4.0.x (Spring Framework 7, namespace Jakarta), baseline de compilação em Java 17 (executável em Java 17 e 25) e Togglz atualizado, mantendo o exemplo de feature toggles já existente (enum de features, controle via JMX e console Togglz) funcionando.

Hoje o projeto usa Spring Boot 2.0.4, a configuração `compile` do Gradle (removida no Gradle 9), Togglz 2.6.1 (namespace `javax`) e depende de um wrapper na raiz do repositório. Nada disso compila/roda no ferramental atual.

## Contexto

O módulo é um exemplo pequeno e autocontido, sem testes. Estrutura atual:

- `Main` — `@SpringBootApplication` que registra um bean `FeatureManager` (in-memory, `NoOpUserProvider`).
- `FeatureSwitch` — enum de features (`MY_FIRST_JOB` habilitada por padrão, `FEATURE_TWO` desabilitada) com API de `isActive`/`getValue`/`setValue`.
- `FeatureJmx` — `@ManagedResource` que expõe consulta e alteração de features via JMX.
- `application.properties` — habilita o console Togglz em `/actuator/features`.

Restrições relevantes:

- Spring Boot 4.0 exige Java 17 como baseline mínimo; por isso "compatível com Java 11" foi descartado explicitamente.
- Togglz 4.x e Spring Boot 4 usam namespace Jakarta; dependências antigas em `javax` são incompatíveis.
- `thymeleaf-extras-togglz:1.0.1` não é usado (não há templates Thymeleaf) e é incompatível com o stack novo.

## Escopo

- Adicionar Gradle wrapper próprio ao módulo `spring-togglz`, na última release stable do Gradle (linha 9.x);
- Reescrever `build.gradle` para Spring Boot 4.0.x com toolchain Java 17 e configurações de dependência atuais (`implementation`);
- Atualizar Togglz para 4.6.4 e remover a dependência não utilizada `thymeleaf-extras-togglz`;
- Ajustar o código-fonte ao stack novo mantendo o comportamento do exemplo (features, JMX e console);
- Adicionar um teste de integração que comprove o exemplo Togglz funcionando;
- Validar build e execução local da aplicação.

## Decisões e regras

### Build e toolchain

| Item | Decisão |
|---|---|
| Gradle wrapper | Wrapper próprio dentro de `spring-togglz` na última stable da linha 9.x |
| Plugin de build | `org.springframework.boot` + `io.spring.dependency-management` (BOM do Spring Boot 4.0.x) |
| Toolchain | `JavaLanguageVersion.of(17)` — compila em 17; deve executar em Java 17 e 25 |
| Configuração de dependência | `implementation`/`testImplementation` (a antiga `compile` foi removida no Gradle 9) |
| Repositórios | `mavenCentral()`; remover `mavenLocal()` |

### Dependências

| Dependência | Decisão |
|---|---|
| `org.springframework.boot:spring-boot-starter-web` | Versão gerenciada pelo BOM Spring Boot 4.0.x |
| `org.togglz:togglz-spring-boot-starter` | `4.6.4` |
| `org.togglz:togglz-console` | `4.6.4` |
| `com.github.heneke.thymeleaf:thymeleaf-extras-togglz` | Removida (não utilizada e incompatível) |
| `org.springframework.boot:spring-boot-starter-test` | Adicionada em escopo de teste |

### Comportamento a preservar

- O bean `FeatureManager` continua registrando o enum `FeatureSwitch`, com `InMemoryStateRepository` e `NoOpUserProvider`.
- `MY_FIRST_JOB` permanece ativa por padrão (`@EnabledByDefault`); `FEATURE_TWO` permanece inativa por padrão.
- A API pública de `FeatureSwitch` (`isActive`, `getValue`, `setValue`) e o `FeatureJmx` permanecem funcionando com o mesmo contrato observável.
- O console Togglz permanece acessível em `/actuator/features` (as chaves `togglz.console.*` devem ser ajustadas caso tenham sido renomeadas no Togglz 4.x/Spring Boot 4).

### Ajustes de código

- Imports e APIs devem ser alinhados ao Togglz 4.x e ao Spring Framework 7 (Jakarta). Onde a API 4.x for equivalente, o código permanece inalterado.
- Anotações redundantes (ex.: `@EnableAutoConfiguration` sobre `@SpringBootApplication`) devem ser removidas por clareza, sem alterar comportamento.

## Testes

- component test (`@SpringBootTest`, sufixo `CompTest`): comprova que o contexto Spring sobe, que `MY_FIRST_JOB` está ativa por padrão, que `FEATURE_TWO` está inativa por padrão e que alternar uma feature via `FeatureSwitch.setValue`/estado passa a refletir em `isActive`/`getValue`. Este é o teste que garante o exemplo Togglz de ponta a ponta.
- unitários: não se aplicam de forma relevante — a lógica é delegada ao `FeatureManager` do Togglz; testes unitários com mock do framework seriam artificiais.
- console HTTP: fora de cobertura de teste automatizado, por decisão de escopo (será validado manualmente).
- fixtures/Templates: não necessários — os dados do cenário são o próprio enum de features, sem massa de dados a montar.

## Fora do escopo

- Suporte a Java 11 (incompatível com Spring Boot 4);
- Adição de templates Thymeleaf ou reintrodução de integração Thymeleaf/Togglz;
- Teste automatizado do endpoint do console Togglz;
- Integração do módulo ao wrapper/settings da raiz do repositório (permanece standalone);
- Reestruturação em camadas DDD — incompatível com a natureza de exemplo mínimo do módulo.

## Validação

Após a implementação:

1. `./gradlew build` no módulo `spring-togglz` compila e executa o component test com sucesso, usando toolchain Java 17;
2. Subir a aplicação localmente (`./gradlew bootRun`) e confirmar boot sem erros e o console Togglz respondendo em `/actuator/features`;
3. Revisão do diff conforme as skills `principal-dev` e `test-dev`;
4. Executar o build de validação:

```shell
./gradlew build
```

> Observação: o comando padrão `./gradlew build compTest` do template não se aplica — este módulo standalone não expõe a task `compTest`; os component tests rodam dentro de `build`.

## Critérios de aceite

- O módulo compila e roda com Gradle wrapper próprio na última stable da linha 9.x;
- A aplicação sobe em Spring Boot 4.0.x, compilada em Java 17 e executável em Java 17 e 25;
- Togglz atualizado para 4.6.4; `thymeleaf-extras-togglz` removida;
- Exemplo Togglz preservado: `MY_FIRST_JOB` ativa por padrão, `FEATURE_TWO` inativa, toggle via `FeatureSwitch` e `FeatureJmx` funcionando, console em `/actuator/features` acessível;
- O component test de integração passa e cobre os estados padrão e a alternância de feature;
- O comando de validação definido nesta Spec passa integralmente.
