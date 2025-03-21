# Getting Started

It is test technical task for the company [Veikkaus](https://www.veikkaus.fi/)
Veikkaus is a reactive Spring Boot microservice for managing players, 
their wallets, and game events. 
It is built using reactive programming principles with 
Project Reactor, R2DBC, and an PostgreSQL-compatible database.
### TL;DR
* DockerHub image: [varg/veikkaus](https://hub.docker.com/r/varg/veikkaus)
* Application work on port `8080` by default
```shell
docker run -p 8080:8080 varg/veikkaus
```
or docker compose
```shell
docker-compose up .
```
#### Env variables
* **DB_NODES** : Database nodes _(default: localhost:5432)_
* **DB_NAME** : Database name _(default:veikkaus)_
* **DB_USER** : Database user _(default: postgres)_
* **DB_PASSWORD** : Database password _(default: postgres)_
* **DB_MASTER_NODE** : Database master node _(for liquibase migration, default: localhost:5432)_
* _DB_FAILOVER_ : Database failover _(for multihost, ex: r2dbc:postgresql:failover://)_

#### Features
* Create and manage player accounts
* Charge or credit player balances based on game events
* Prevent duplicate events and duplicate players

### Tech Stack

* Java 21+
* Maven 3.9.5
* PostgreSQL 14+

### API
* for POST request used Basic Auth with username `user` and password `password`
* The Swagger UI page will then be available at the following url:
  [http://server:port/swagger-ui.html](http://localhost:8080/swagger-ui.html)
  and the OpenAPI description will be available at the following url for json format: [http://server:port/v3/api-docs](http://localhost:8080/v3/api-docs)
    * server: The server name or IP
    * port: The server port
    * context-path: The context path of the application

#### Request examples:

##### Player creation 
```shell
curl --location 'http://127.0.0.1:8081/player' \
--header 'Content-Type: application/json' \
--data '{
  "name": "Evgenii"
}'
```

### Builds

#### Build and run
```shell
./mvnw spring-boot:run
```
or 
```shell
./mvnw clean package
java -jar target/veikkaus-0.0.1-SNAPSHOT.jar
```

##### Build with docker
```shell
docker build -t veikkaus-app .
docker run -p 8080:8080 veikkaus-app
```

### Testing

application-test.yaml is used for integration and contract tests.

Ensure both Liquibase and R2DBC point to the same in-memory database:
```yaml
spring:
  r2dbc:
    url: r2dbc:h2:mem://localhost/testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
  liquibase:
    url: jdbc:h2:mem://localhost/testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
```
### Troubleshooting
Liquibase tables not found during test: Ensure R2DBC and JDBC share the same memory database (localhost/testdb).
Contracts not generating: Ensure spring-cloud-contract-maven-plugin has correct baseClassForTests configured.


### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.2/maven-plugin/reference/html/)
* [OpenAPI Generator](https://openapi-generator.tech/)
* [OpenAPI Description](https://learn.openapis.org)
* [Docker documentation](https://docs.docker.com)
* [PostgreSQL documentation](https://www.postgresql.org)
