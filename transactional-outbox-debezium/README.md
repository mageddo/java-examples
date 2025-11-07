```bash
docker-compose up
```
```bash
curl -i -X POST http://localhost:8083/connectors \
  -H "Content-Type: application/json" \
  --data @src/main/docker/kconnect/connector-tto-record.json
```
