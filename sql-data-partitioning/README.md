### Finding skin price by id

```bash
$ curl -i -s -w '\n' http://localhost:8080/v1/skin-prices/2018-12-01_05
HTTP/1.1 204 
Date: Sun, 23 Dec 2018 23:18:42 GMT
```

### Creating skin price

```bash
$ curl -X POST -i -s -w '\n' \
-H 'Content-Type: application/json' \
--data-binary '{"hashName":"M4A1-S | HOT ROD (FACTORY NEW)","occurrence":"2018-12-23T21:32:20.114094","price":38.8100}' \
http://localhost:8080/v1/skin-prices
HTTP/1.1 200 
```

### Interesting links
* [table partitioning & max_locks_per_transaction](https://www.postgresql.org/message-id/26276.1255229812%40sss.pgh.pa.us)
* https://dba.stackexchange.com/questions/193402/why-declarative-partitioning-on-postgresql-10-is-slower-than-the-non-partitioned
* https://blog.2ndquadrant.com/partition-elimination-postgresql-11/#comment-262750

### Reports
* [PG 10 - Partitioning by Month (18 partitions) - empty table](https://mageddo.github.io/java-examples/sql-data-partitioning/docs/report-01/report)
	* [Considerations](https://mageddo.github.io/java-examples/sql-data-partitioning/docs/report-01/)
* [PG 10 - Partitioning by Month - table with a preload of 50M](https://mageddo.github.io/java-examples/sql-data-partitioning/docs/report-02/report)
	* [Considerations](https://mageddo.github.io/java-examples/sql-data-partitioning/docs/report-02/)
* [PG 11 - Partitioning by Month - table with a preload of 50M](https://mageddo.github.io/java-examples/sql-data-partitioning/docs/report-06/report)
	* [Considerations](https://mageddo.github.io/java-examples/sql-data-partitioning/docs/report-06/)
* [PG 10 - Non partitioning](https://mageddo.github.io/java-examples/sql-data-partitioning/docs/report-03/report)
	* [Considerations](https://mageddo.github.io/java-examples/sql-data-partitioning/docs/report-03/)
* [PG 10 - Partitioning by Day (400 partitions) - empty table](https://mageddo.github.io/java-examples/sql-data-partitioning/docs/report-04/report)
	* [Considerations](https://mageddo.github.io/java-examples/sql-data-partitioning/docs/report-04/)

### Conclusion

__Non partitioned table__
* Selects by UNIQUE key are faster than on partitioned tables
* Selects by range will be slower
* If table receives many updates Selects by range range will be much worse
* Insert performances degrade linearly by the time that size increases

__Partitioned table__
* Selects by UNIQUE key are a bit slower than on non partitioned tables (0 vs 1ms)
* Selects by range tends to be faster than on non partitioned tables even if updates are intensive
* Inserts performance tends to be constant because a new partition will be used when the current start to get "too big"

__Table with a high number (400) of partitions__ 
* Create too many partitions for one table don't seems to be a good idea it will force
	Postgres to do too much overhead for every insert / select
* Postgres do locks every subtable on every insert/select then your statement will be more slower as the number of partitions you have
* [Don't create too much partitions](https://stackoverflow.com/a/26417922/2979435)
* As a workaround you can select/insert directly from partition

__Postgres 10 vs Postgres 11__

* Selects are faster on Postgres 11 because of [partition pruning](https://www.postgresql.org/docs/11/ddl-partitioning.html#DDL-PARTITION-PRUNING)
* Pruning only works for selects BTW 
