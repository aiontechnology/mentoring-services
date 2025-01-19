# About

This project vends a REST API for use with the mentoring front end. It uses 
the following technologies:

1. Java 14
1. Gradle
1. Spring Boot 2.3.4 (and related Spring technologies)
1. Postgres

# Running the Server

There are several ways to run the server locally. In either case, the Java 13 
JDK and Gradle are needed. The Gradle wrapper can be used to install it.

## In Memory Database

It is possible to run the server without a dedicated database. This can be done 
with the following command:

> ./gradlew bootRun --args="--spring.profiles.active=h2"

The advantage of this approach is that there is no need to set up an external
Postgres database. The disadvantage is that you will loose all of your data
each time the server is restart.
 
## Running Postgres in Docker

Running Postgres as a Docker container is not too much more difficult. Once
you have Docker installed on your machine, you simply use this comment to
launch a servier instance:

> docker run --name postgres -e POSTGRES_PASSWORD=\<db password\> -P -d postgres

Once Postgres is running, use a tool such as `pgAdmin` to log into Postgres
and create a new database called `mentorsuccess`.

Now check the port on which Postgres is running using:

> docker ps

Then run the server with this command from the `server` directory:

> ../gradlew bootRun --args="--spring.profiles.active=postgres \
> --spring.datasource.url=jdbc:postgresql://localhost:\<port\>/mentorsuccess \
> --spring.datasource.password=\<db password\>"