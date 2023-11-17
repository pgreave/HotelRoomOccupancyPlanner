# An example Spring Boot application

## Prerequisites

1. Java 17
2. Gradle (optional) - project is using Gradle Wrapper

## Running application

Preferred way to run application is via IntelliJ IDEA.
Main application class is `pl.s.h.interview.Main`

Optionally you can start application using command-line terminal:
```shell
./gradlew bootJar
```

### REST API
After successful application startup, REST API should be available under path:
`http://localhost:8080/api/occupancy/plan/build`

An example request can be found in project folder `requests/OccupancyPlanBuildRequest.http`. 

## Running tests
Preferred way to run application tests is via IntelliJ IDEA.
Tests are split into two folders:
- integration tests in `src/integration-test`
- unit tests `src/test`

Optionally you can start tests using command-line terminal.
#### Unit tests:
```shell
./gradlew tests
```
#### Integration tests:
```shell
./gradlew integration-tests
```
