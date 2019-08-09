```bash
$ curl -s localhost:8080/api/users | jq '.'
{
  "timestamp": "2019-08-09T21:04:10.264+0000",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/users"
}


$ curl -s -H 'Authorization: Bearer e4549fa4-49ca-4fee-845b-9d21e64088d8' localhost:8080/api/users | jq .
[
  "Joao",
  "Maria"
]
```
