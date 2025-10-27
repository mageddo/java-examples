### Dev

* length: 189

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
2025-10-27 19:18:49.218 app=RECEIVABLE-UNIT-LISTENER - INFO 50849 --- [           main] com.pagbank.logging.App                                      : M=afterPropertiesSet action=helloWorld
```

### Prod

* length: 107

```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
2025-10-27 19:26:03.048 l=INF t=main l=com.pagbank.logging.App m=afterPropertiesSet  action=helloWorld
```
