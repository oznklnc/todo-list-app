# TodoList Application

> ### Spring Data + Couchbase + Spring Security + Spring Boot + Swagger

## Overview
This application is a simple Todo List Application created for demonstration purposes. It uses Spring Boot, Spring Data, Couchbase, Spring Security, and Swagger for API documentation.

## Prerequisites
- Java 17
- Maven
- Docker (for running Couchbase Server)



```properties
couchbase.connectionString=couchbase://localhost
couchbase.username=Administrator
couchbase.password=password
couchbase.bucketName=todo
couchbase.scopeName=_default
```

## Running the Application
### Using Maven
1. Navigate to the project directory.
2. Run the application:`mvn spring-boot:run`

### Using Docker
1. Build the all Docker images and uses for local. You can use the following command: 
```
docker-compose up -f docker-compose-local.yml -d
```
2. Build the all Docker images and uses from dockerhub. You can use the following command: 
``` 
docker-compose up -f docker-compose-todo-app.yml -d
``` 


## Running Tests
To run the unit tests, use the following Maven command:
`
mvn test
`
## Build your app
To build the application, use the following Maven command:
`
mvn clean install -Pall-tests
`
## Run the test suite
To run the test suite, use the following Maven command:
`
mvn test -Pall-tests
`

## Run your app with dependencies
To run your Spring Boot application with dependencies, use the following Maven command:
`
mvn spring-boot:run
`
If you need to specify a particular profile, you can use the <span style="color:red">-P</span> flag followed by the profile name. You can find three profiles. For example, to run the application with the <span style="color:red">unit-tests</span> profile:

1. Run `mvn spring-boot:run -Punit-tests`
2. Run `mvn spring-boot:run -Pintegration-tests`
3. Run `mvn spring-boot:run -Pall-tests`
```
mvn spring-boot:run -Punit-tests
```

## API Documentation

API documentation is available via Swagger UI. Once the application is running, you can access it at:
> **Link : http://localhost:8080/swagger-ui.html**

## Couchbase UI
> **Link : http://localhost:8091/**
