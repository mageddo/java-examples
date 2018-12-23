#### Finding skin price by id

```bash
$ curl -i -s -w '\n' http://localhost:8080/v1/skin-prices/2018-12-01_05
HTTP/1.1 204 
Date: Sun, 23 Dec 2018 23:18:42 GMT
```

#### Creating skin price

```bash
$ curl -X POST -i -s -w '\n' \
-H 'Content-Type: application/json' \
--data-binary '{"hashName":"M4A1-S | HOT ROD (FACTORY NEW)","occurrence":"2018-12-23T21:32:20.114094","price":38.8100}' \
http://localhost:8080/v1/skin-prices
HTTP/1.1 200 
```
