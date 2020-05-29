# code-with-quarkus project

This is a sample of running Quarkus + Kafka + Postgres + JDBI + Thymeleaf + ReactJs 

# Running

Starting Postgres, Kafka

```bash
docker-compose up
```

Start the quarkus java backend

```bash
$ ./gradlew quarkusDev
```

Start reactjs frontend

```bash
$ cd application/frontend &&  ./node_modules/.bin/react-scripts start
```

Access http://localhost:8084
