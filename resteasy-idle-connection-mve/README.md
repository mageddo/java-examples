# RESTEasy: timeout após conexão ociosa

Projeto independente para testar reutilização de conexões HTTPS com RESTEasy e Apache HttpClient. O servidor e o proxy são locais: nenhum serviço externo, token ou arquivo da aplicação é utilizado.

## Executar

Requisitos: Java21, Python3 e OpenSSL disponíveis. O wrapper usa Gradle9.5.1 e resolve as dependências pelo Maven Central.

```sh
./gradlew build compTest
```

O comando compila o client, executa40 chamadas HTTPS e verifica os resultados. Duração padrão: aproximadamente80s. Para repetir somente o experimento:

```sh
./gradlew runMve
```

O tempo de inatividade e o timeout de leitura podem ser configurados em milissegundos:

```sh
./gradlew runMve -PidleMillis=60000 -PreadMillis=1000
```

A matriz completa com um minuto de inatividade demora aproximadamente20min. `idleMillis` precisa ser maior que2000ms, para superar os limiares usados pela simulação e pela validação de conexões do Apache; `readMillis` precisa ser positivo. Para testar apenas o proxy com uma espera maior que15s:

```sh
./gradlew runMve -Pscenario=blackhole -PidleMillis=16000
```

O servidor continua mantendo os sockets abertos mesmo após longos períodos ociosos. O processo de coleta encerra seus próprios processos Java/Python ao terminar ou falhar.

## O que é testado

O servidor atribui um ID para cada conexão e devolve esse ID e o número da requisição na resposta.

| Cenário | Comportamento esperado |
|---|---|
| Saudável | Duas chamadas separadas por inatividade reutilizam a mesma conexão e funcionam. |
| Fechamento explícito | Servidor fecha o socket após1s. Apache detecta e usa uma conexão nova. |
| Servidor silencioso | Servidor recebe a segunda chamada na conexão antiga, mas não responde. Client recebe read timeout. |
| Proxy silencioso | Proxy encaminha o túnel TLS inicialmente; após inatividade maior que1s, aceita e descarta dados sem fechar os sockets. A segunda chamada não chega ao servidor HTTP saudável. |
| Client novo | Trocar o client antes da segunda chamada cria conexão nova e evita o problema simulado. |
| TTL de1s | Limitar a idade da conexão impede reutilizar a conexão antiga no experimento. |
| Descarte de ociosas | Fechar conexões ociosas por mais de1s antes da segunda chamada evita reutilizar a conexão antiga. |

A matriz executa esses cenários em TLS1.3 e TLS1.2. A verificação confere status, tipo de erro, IDs de conexão e eventos do proxy. No cenário isolado de proxy, executa quatro chamadas: primeira e segunda em cada versão TLS.

## Configuração do client

- Java21; reprodução original em Temurin21.0.10.
- RESTEasy6.2.12.Final; Apache HttpClient4.5.14; HttpCore4.4.16.
- Pool com5 conexões totais e por rota; timeout para obter conexão3s; conexão500ms; leitura1s por padrão.
- Baseline sem TTL nem descarte de ociosas; validação padrão do Apache após2s de inatividade.
- Engine `ApacheHttpClient43Engine`, preservando `RequestConfig` após `super.loadHttpMethod`, como o wrapper investigado. O probe verifica que o timeout padrão foi preservado.
- Certificado gerado a cada execução, confiado apenas pelo client do experimento. A verificação do hostname permanece ativa.

O engine e a API de gerência de conexões usados para reproduzir o transporte original estão depreciados upstream; os avisos de compilação ficam visíveis.

## Evidências e interpretação

Resultados em `build/results/matrix/`, ou `build/results/blackhole/` para o cenário isolado:

- `TLSv1.3.log` e `TLSv1.2.log`: tempos de chamada, status, IDs e exceções.
- `server.jsonl`: cada conexão, requisição, resposta, fechamento e descarte de dados pelo proxy.
- `cert.pem` e `key.pem`: certificado e chave locais descartáveis.

A compilação gera `build/runtime-classpath.txt` a partir das dependências resolvidas pelo Gradle. O projeto não depende de caminhos fixos de cache ou de arquivos temporários externos.

A POC original mostrou conexões saudáveis funcionando após inatividade e fechamentos explícitos detectados. A conexão silenciosamente inutilizada passou pela checagem local e levou a timeout; conexão nova, TTL e descarte de ociosas evitaram o erro simulado.

No servidor silencioso, TLS1.3 levou aproximadamente2s para devolver um timeout de leitura1s; TLS1.2 levou1s. No proxy, ambas levaram aproximadamente1s. Portanto, demora adicional no fechamento TLS é possível, mas não acompanha necessariamente todo timeout TLS1.3.

O timer de requisição exclui a troca explícita do client e o descarte explícito de ociosas. A expiração por TTL ocorre dentro da chamada e entra no tempo medido. Esses tempos não são medidas de latência total de uma aplicação.

## Limites

Este experimento constrói a falha intencionalmente para comprovar um mecanismo possível. Não demonstra que Google, OpenAI, Nexoos ou algum equipamento da rede real causou os episódios investigados. O proxy mantém duas conexões TCP abertas e descarta ciphertext; não reproduz perda de pacotes no kernel nem mede o timeout real de um NAT.

Não testa OAuth, processamento do fornecedor, suspensão, DNS ou políticas de retry. TTL limita idade total; descarte de ociosas limita inatividade: são políticas diferentes. O build e a matriz local validam este projeto diagnóstico, sem alterar ou executar a aplicação original.
