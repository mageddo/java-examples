Essa é a melhor versão até agora.

### Features
* Edição em tempo real para multiplos usuários
* Visualização de quem está editando e em que linha está trabalhando
* Caso servidor caia e perca as versões, e o client permaneça aberto,
 ele refaz o sync com o servidor quando o servidor voltar

### Features Técnicas
* NPM para evitar o dependency hell
* Conflito todo tratado no client, server só salva o estado em formato byte[]
* De tempos em tempos client manda snapshot para o server
* Funciona em mobile
  * Texto selecionável

## Rodando

```bash
./gradlew quarkusDev
```

Acesse http://localhost:8585

Se quiser recompilar o frontend

```bash
npm run build
```
