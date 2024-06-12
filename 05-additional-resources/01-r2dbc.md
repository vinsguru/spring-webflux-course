## Resources for R2DBC

### Database Initialization Scripts

```properties
# For db initialization scripts
spring.sql.init.data-locations=classpath:sql/data.sql

# To show SQL
logging.level.org.springframework.r2dbc=TRACE
```

### Connection String

```properties
# H2
r2dbc:h2:mem:///userdb

# Postgres
r2dbc:postgresql://localhost:5432/userdb

# Mysql
r2dbc:mysql://localhost:3306/userdb
```

### Additional Resources

- [R2DBC Documentation](https://r2dbc.io/spec/1.0.0.RELEASE/spec/html/)
- [R2DBC Drivers](https://r2dbc.io/drivers/)
- [R2DBC Data Types](https://r2dbc.io/spec/1.0.0.RELEASE/spec/html/#datatypes)
- [Spring Data R2DBC](https://docs.spring.io/spring-data/relational/reference/r2dbc.html)
- [Spring Query Methods](https://docs.spring.io/spring-data/relational/reference/r2dbc/query-methods.html)