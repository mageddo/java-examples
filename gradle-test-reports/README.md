# Gradle Test Reports

Aplicação web (Quarkus) para explorar os relatórios de teste de projetos Gradle
a partir de um diretório. A jornada tem 4 passos, cada um com URL própria e
compartilhável:

1. **Diretório** — você informa um caminho absoluto.
2. **Projetos** — o servidor varre o diretório e lista os projetos Gradle.
3. **Resultados** — ao abrir um projeto, lista as suítes em `build/test-results`
   (ex.: `test`, `componentTest`) com nome, tempo total da suíte, total de
   testes, passados, com erro e ignorados.
4. **Relatório detalhado** — ao abrir uma suíte, mostra a tabela de testes com
   busca, filtros, ordenação, barra de duração e exportação (CSV/JSON).

O diretório informado no passo 1 é salvo no navegador (`localStorage`), evitando
retypá-lo a cada visita.

## Stack

* Quarkus 3.33.3
* Java 21 (toolchain configurado no `build.gradle`)
* REST (`quarkus-rest` + `quarkus-rest-jackson`)
* Frontend estático em `src/main/resources/META-INF/resources`
  (Bootstrap 5, jQuery, bootstrap-table)

## Como rodar

### Dev mode (com hot reload)

```bash
./gradlew quarkusDev
```

Acesse http://localhost:8080

> O hot reload (live coding) só funciona com `quarkusDev`. Alterações em
> classes Java, HTML, CSS e JS são refletidas sem reiniciar. `quarkusRun`
> executa o artefato empacotado e **não** recarrega.

### Build de produção

```bash
./gradlew build
java -jar build/quarkus-app/quarkus-run.jar
```

## API REST

Todos os endpoints são idempotentes e repetíveis por URL:

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/projects?dir={dir}` | Lista os projetos Gradle sob o diretório. |
| GET | `/api/results?project={projectDir}` | Lista as suítes de teste do projeto. |
| GET | `/api/tests?project={projectDir}&result={suite}` | Lista os casos de teste de uma suíte. |

Exemplo:

```bash
curl "http://localhost:8080/api/projects?dir=/Users/voce/dev/meu-projeto"
```

## Estrutura

```
src/main/java/com/mageddo/gradletestreports
├── TestReportsService.java          # orquestração
├── project/                         # scan de projetos Gradle
├── testresult/                      # parsing de JUnit XML e agregação
└── entrypoint/                      # Resource REST, VOs (*Res) e mapper

src/main/resources/META-INF/resources
├── index.html      + static/home
├── projects.html   + static/projects
├── results.html    + static/results
├── report.html     + static/report
└── static/shared   # style.css e app.js compartilhados
```
