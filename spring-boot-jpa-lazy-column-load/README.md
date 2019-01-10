Prevent specific column load on select, marking as lazy

Testing

```bash
$ ./gradlew clean spring-boot-jpa-lazy-column-load:run
Hibernate: insert into person (id, name) values (default, ?)
Hibernate: select person0_.id as id1_0_ from person person0_ where person0_.id=?
Hibernate: select person_.name as name2_0_ from person person_ where person_.id=?
```

As you can see in the log, hibernate did the first select without the name (lazy column) 
then did select the  lazy column when get was called

