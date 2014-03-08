Wellington is a fully loaded Spring Boot Starter project
===============

This project is meant to provide a base for Spring Boot development.  The idea is to be able to
git clone this project and get right down to implementing features.

### A look inside ###

- Docker integration
    - Automating the packaging and deployment of applications
    - Automated testing and continuous integration/deployment

- AngularJS Starter
    - Based on Angular seed
    - Basic Admin UI provided that leverages spring boot management endpoints

- Thymeleaf Server side templates
    - Seamless integration with Spring security

- Webjars integration
    - Helps manage versions of Angular, Jquery, and Bootstap via gradle

- Built with Gradle
    - gradle wrapper for auto installation
    - dependency management
    - automated tests
        - ./gradlew test
            - executes unit tests
            - generates code coverage report
            - checks code style
            - checks code quality
    - automated code coverage
        - ./gradlew jacocoTestReport
    - Liquibase database refactoring
        - ./gradlew update
        - Incremental database design
        - Rollback
    - functional tests
        - ./gradlew npm (installs node dependencies)
        - ./gradlew protractorRun (runs functional tests)
            - tests defined in src/test/javascript/e2e/scenarios.js
            - leverages protractor support for AngularJS
    - javascript best practices built in
        - [jshint](http://jshint.com/)
        - combination
        - minification
        - source map

- Java Based Spring Configuration via annotations

- Spring Hibernate JPA
    - CrudRepository
    - Hibernate Events
    - Transaction Management
    - Entity Versioning
    - Entity Auditing

- JOOQ type safe query generation
    - gradle task to generate DSL classes from the database
        - ./gradlew jooqGen
    - used in conjunction with spring jdbc template to leverage spring transaction support
    - helper classes to support pagination of results

- Configured to use the Tomcat JDBC Connection Pool
    - well maintained, small connection pool implementation
    - multi threaded

- DAO based Spring security
    - REST endpoints authorized via authorities
    - Spring Service interfaces authorized via authorities
    - Password digests using jasypt
        - Algorithm: SHA-256.
        - Salt size: 16 bytes.
        - Iterations: 100000.

- JSR 303 Validation
    - Validation groups
    - Validation at the Controller level
    - Validation at the Entity level
    - Validation of External Configuration

- JMS Messaging via ActiveMQ
    - JMSTemplate
    - MessageListenerContainer

- REST Controller
    - Base REST Error Handler
        - Business Exception
        - Data Integity Exception
        - Validation Exception
        - Malformed Input Exception
    - Straightforward JPA data paging / REST page response
    - JSON payloads

- Ehcache integration
    - Annotation based caching and cache eviction
    - Caching of User authentication info

- Spring AOP
    - Aspect Oriented programming via Annotations

- YAML configuration files

- Object mapping via Dozer

- Logging via Logback

- Automatic management endpoints via Spring Actuator
    - /health
    - /metrics
    - /trace
    - /dump
    - /beans
    - [More Endpoints](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/endpoint)
    - Endpoints secured with Spring Security

### Running the application with docker ###

Prerequisites

- [Docker](https://www.docker.io/)

Build the docker image

    sudo docker build -t spring .

Run the docker image with port mapping

    sudo docker run -p 8080:8080 -i -t spring


You should now be able to access the application on your host system by visiting http://localhost:8080


#### Running the application without docker ####

Prerequisites

- [Java 7](http://www.oracle.com/technetwork/java/javase/overview/index.html)
- [Apache ActiveMQ](http://activemq.apache.org/)
- [Postgresql](http://www.postgresql.org/) Database (empty database named 'spring')
- [nodejs] (http://nodejs.org/)

Start ActiveMQ (from activeMQ HOME)

    bin/activemq start

Create database tables via Liquibase (from project dir)

    ./gradlew update

Run all tests and generate test reports

    ./gradlew test

Run the Web App on port 8080 (from project dir)

    ./gradlew bootRun

#### Verify the Application ####

By default the system will have 1 admin user with credentials (admin/admin).

You can login at http://localhost:8080.

You can register new users by clicking the Register link before login.

#### Running the functional tests ####

By default the chrome web browser is used.  Update protractor-conf.js to change this.

Install node dependencies (do once and then anytime package.json is updated)

    ./gradlew npm

Start the app if not already started

    ./gradlew bootRun

Run protractor

    ./gradlew protractorRun

Check output

    build/test-results/TEST-Wellington.xml


#### Setting up an IDE for development ####

Eclipse

    ./gradlew eclipse

IntelliJ IDEA

    ./gradlew idea

#### Running inside a servlet 3 web container ####

Add the war plugin to build.gradle

    apply plugin 'war'

Change the build target from jar to war in build.gradle

    jar {
        baseName = 'wellington'
        version =  '0.1.0'
    }

    becomes

    war {
        baseName = 'wellington'
        version =  '0.1.0'
    }

You can now build a war file to deploy to your container

    ./gradlew clean war

#### Production Mode ####

Flip a switch in application.properties in order to run in production mode

    info.app.development=false

This switches to using the minified version of the application javascript.

#### License ####
Wellington is released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0).



