# Auth Service

This service is responsible for managing users credentials and sessions by generating JWT tokens in order to access specific, protected services with sensible information.  

#### Requirements
- Java 16
  - If you don't have it:
    - https://sdkman.io/install
  - Then in *Terminal* (this example uses GraalVM, but it can be any Java16 implementation):
    - `sdk install java 21.2.0.r16-grl`
    - `sdk use java 21.2.0.r16-grl`

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ 

### First things first: Building the service (locally)
`./mvnw package`

#### Running the Service after building it

There are many ways to run it: For a quick test with all required resources loaded automatically, in a docker environment or simply running pointing to a specific database/running instance.

Once it is running, you can check the `http://localhost:8080/swagger-ui` to invoke the endpoints

### Dry-run (dev mode with a test container postgrelsql instance)
It will initialise the app and will also spin-up a PSQL instance and point to it. Press Q in order to stop it (and will also stop the container) 

- `./mvnw quarkus:dev`
- Then just access `http://localhost:8080/swagger-ui` so you can see the service documentation, invoke the desired endpoint and so on

### Locally with Docker Compose
It will start both auth-service and database, in prod mode. If the container is not build, it will do it automatically:
- Initialise docker-compose by executing `docker-compose up`
- The endpoint will be available on `http://localhost:8080/swagger-ui`

#### In order to access the running docker postgresql, you can:
- `docker ps`, get the pid
- `docker exec -it <pid> psql -h localhost -p 5432 -U dc_authsvcuser -d dc_authservicedb -w dc_authsvcpass`
- `\dt` to show current tables, or `select * from users` to show all current users

### Prod mod (k8s, your own server etc)
- `docker build . -t yourtag`
- Push it to your docker repo and point in your k8s manifest.

### Simple execution
`AUTH_DB_HOST=<hostname> AUTH_DB_NAME=<database name> AUTH_DB_USER=<database username> AUTH_DB_PASS=<password> java -jar target/quarkus-app/quarkus-run.jar`

### Useful endpoints
- `/swagger-ui`: Swagger documentations
- `/swagger`: OpenAPI definitions
- `/q/health/live`: Liveness endpoint
- `/q/health/ready`: Readyness endpoint
- `/q/health`: Aggregated health endpoint

# Testing/development

### Unit Testing
Totally isolated, swift sociable tests using classical TDD where only external dependencies are mocked/stubbed/faked (eg database, encryption service).
Used more for development, in order to get quick feedback if something got broken etc.
For context: https://martinfowler.com/articles/2021-test-shapes.html

In order to run them: `./mvnw test`

### Integration Testing
Testing only integration layers (eg repositories, hashing classes). They don't have any business logic, just enough code to interact to external dependencies and ensure their behaviours is what the classes are expecting.

Command: `./mvnw verify -Pintegration`

### Acceptance Testing
An end-to-end test suite, where HTTP calls are made against a local, running instance of auth-service. It will access a PostgreSQL instance, a Bcrypt hashing and will validate JWT tokens/calls. 

Command: `./mvnw verify -Pacceptance`

### Unit/Integration/Acceptance all at once
Command: `./mvnw verify -Pall`

### Sanity/Smoke Testing
A test against a running remote instance. It can be used to check if a staging/prod env is running and doing a basic operation and checking the health endpoints.

Command: `./mvnw verify -Psanity -DAUTH_SERVICE_HOST=<hostname:port>` (default localhost:8080)

## Limitations

This service cannot be built in native-mode (one of the major advantages of quarkus) due to some limitations related to java records, hence the usage of JVM-based docker images instead of native unix binaries. 