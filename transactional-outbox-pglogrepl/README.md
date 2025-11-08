```bash
docker-compose up
```

```bash
export CONN_STRING="postgres://kconnect:kconect@127.0.0.1:5436/db?sslmode=disable"
export RELATION_NAME="public.OUTBOX_RECORD"
go run main.go
```

```
```
