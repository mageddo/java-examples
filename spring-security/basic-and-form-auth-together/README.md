Autenticar de duas formas diferentes no mesmo spring baseado no path


Usando form login

```bash
$ curl -w '\n' -i -s localhost:8080/admin
HTTP/1.1 302 
Set-Cookie: JSESSIONID=C7EDB51197D0A6F0E6CA59E634CB6600; Path=/; HttpOnly
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Location: http://localhost:8080/login
Content-Length: 0
Date: Tue, 24 Jul 2018 04:27:33 GMT
```

Usando o basic auth
```bash
$ curl -w '\n' -i -s localhost:8080/api
HTTP/1.1 401 
Set-Cookie: JSESSIONID=1EB4C72302CF7815E0B8E0D3208CAAF0; Path=/; HttpOnly
WWW-Authenticate: Basic realm="Realm"
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Tue, 24 Jul 2018 04:27:43 GMT

{"timestamp":"2018-07-24T04:27:43.880+0000","status":401,"error":"Unauthorized","message":"Unauthorized","path":"/api"}
```
