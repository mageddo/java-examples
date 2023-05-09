## Advises
* Configure timeouts with the avg server response + 10ms
* Configure the retry strategy to 3 times with avg server response interval.
* Check the spring server you are making calls have a considerable server threads config default = 200 `server.tomcat.threads.max`
* When server threads are exhaust 5 seconds is the default

## My tests 

The expected transaction time is 40ms for the payment authorization + 30-50ms for saving = 70-90ms

```
$ siege -v -c3 -t30S http://localhost:8080/api/v1/checkouts-mock
Lifting the server siege...
Transactions:            913 hits
Availability:          99.78 %
Elapsed time:          29.35 secs
Data transferred:         0.00 MB
Response time:            0.10 secs
Transaction rate:        31.11 trans/sec
Throughput:           0.00 MB/sec
Concurrency:            2.99
Successful transactions:         913
Failed transactions:             2
Longest transaction:          0.32
Shortest transaction:         0.07


# HTTP Server with 200 threads 
$ siege -v -c100 -t30S http://localhost:8080/api/v1/checkouts-mock
Lifting the server siege...
Transactions:            301 hits
Availability:          64.73 %
Elapsed time:           0.98 secs
Data transferred:         0.02 MB
Response time:            0.29 secs
Transaction rate:       307.14 trans/sec
Throughput:           0.02 MB/sec
Concurrency:           90.39
Successful transactions:         301
Failed transactions:           164
Longest transaction:          0.39
Shortest transaction:         0.07
 

# HTTP Server with "infinity" threads
# HTTP client with the double of the median timeout + 10 ms

$ siege -v -c100 -t30S http://localhost:8080/api/v1/checkouts-mock
Lifting the server siege...
Transactions:          30385 hits
Availability:          99.89 %
Elapsed time:          29.33 secs
Data transferred:         0.00 MB
Response time:            0.10 secs
Transaction rate:      1035.97 trans/sec
Throughput:           0.00 MB/sec
Concurrency:           99.75
Successful transactions:       30385
Failed transactions:            34
Longest transaction:          0.61
Shortest transaction:         0.06
 
```
