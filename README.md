# ![RealWorld Example App](logo.png)

> ### Spring Data + Couchbase codebase containing real world examples (CRUD, auth, advanced patterns, etc) that adheres to the [RealWorld](https://github.com/gothinkster/realworld) spec and API.

### [Demo](https://demo.realworld.io/)&nbsp;&nbsp;&nbsp;&nbsp;[RealWorld](https://github.com/gothinkster/realworld)


This codebase was created to demonstrate a realworld backend application built with **Spring Data Couchbase** including CRUD operations, authentication, routing, pagination, and more. For more information on how to this works with other frontends/backends, head over to the [RealWorld](https://github.com/gothinkster/realworld) repo.

# Getting started

This Project requries Java 17, Maven, and a running Couchbase Server instance. See the Setup Couchbase section for more details.

## Configuration

This project needs a configured Couchbase configuration to run properly. There are different ways you can do it. Provide a properties file with the following content:

```properties
couchbase.connectionString=couchbase://localhost
couchbase.username=Administrator
couchbase.password=password
couchbase.defaultBucket=default
couchbase.defaultScope=_default
```

or make sure you have the following environment variables configured:
```
export COUCHBASE_CONNECTIONSTRING=couchbase://localhost
export COUCHBASE_USERNAME=Administrator
export COUCHBASE_PASSWORD=password
export COUCHBASE_DEFAULTBUCKET=default
export COUCHBASE_DEFAULTSCOPE=_default
```

### Test Configuration

To run the postman test, you need to setup the following environment variables:

```
APIURL=http://localhost:8080/api
USERNAME=username
EMAIL=useremail@domain.com
PASSWORD=userPass
```

## Run
### mdögmdklgmgkgkm
1. Run `mvn package` in the project folder to build the project
1. Run `java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/*.jar`
2. Run `./postman/run-api-tests.sh` to run Postman collection tests

The reason we need to add the `--add-opens java.base/java.lang=ALL-UNNAMED` parameters can be found on this article: https://www.springcloud.io/post/2022-07/inaccessibleobjectexception/

## Setup Couchbase

There are multiple ways to create a Couchbase instance.

### Using Docker

You can use our [official Docker image](https://hub.docker.com/_/couchbase) by running `docker run -d --name db -p 8091-8097:8091-8097 -p 9123:9123 -p 11207:11207 -p 11210:11210 -p 11280:11280 -p 18091-18097:18091-18097 couchbase`
Then you can go to http://localhost:8091 and start setting up your cluster. Take a look at the hub page for more details.

### Using Couchbase Capella

Go to https://cloud.couchbase.com. From there you can setup a trial account and get a 30days free instance, no Credit Card required. Check out our getting started here: