Official framework to generate java benchmark tests

### Running it  

```
$ ./gradlew general-samples:jmh
73.406 ±(99.9%) 11.773 ms/op
# Warmup Iteration   2: 80.884 ±(99.9%) 12.842 ms/op
# Warmup Iteration   3: 69.235 ±(99.9%) 10.787 ms/op
# Warmup Iteration   4: 73.332 ±(99.9%) 12.330 ms/op
# Warmup Iteration   5: 74.131 ±(99.9%) 12.806 ms/op
Iteration   1: 74.549 ±(99.9%) 11.947 ms/op
                 measureThroughput·p0.00:   0.006 ms/op
                 measureThroughput·p0.50:   74.056 ms/op
                 measureThroughput·p0.90:   131.937 ms/op
                 measureThroughput·p0.95:   144.336 ms/op
                 measureThroughput·p0.99:   148.405 ms/op
                 measureThroughput·p0.999:  149.160 ms/op
                 measureThroughput·p0.9999: 149.160 ms/op
                 measureThroughput·p1.00:   149.160 ms/op

```

### Links
* [Examples](https://github.com/melix/jmh-gradle-example/tree/master/src/jmh/java/org/openjdk/jmh/samples)
