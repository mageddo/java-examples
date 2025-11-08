```bash
docker-compose up
```

```bash
export CONN_STRING="postgres://kconnect:kconect@127.0.0.1:5436/db?sslmode=disable"
export RELATION_NAME="public.OUTBOX_RECORD"
go run main.go
```

```sql
INSERT into OUTBOX_RECORD values (
    uuid_in(md5(random()::text || clock_timestamp()::text)::cstring),
    'outbox_test_topic',
    'key-1',
    'some value 4',
    null
);
```
