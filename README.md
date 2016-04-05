Wellington
===============
This project is meant to provide a base for Spring Boot development.  The idea is to be able to
git clone this project and get right down to implementing features.

[![Build Status](https://travis-ci.org/rwynn/wellington.svg?branch=master)](https://travis-ci.org/rwynn/wellington)

### A look inside ###

- Docker integration
    - Automating the packaging and deployment of applications
    - Automated testing and continuous integration/deployment

- Vagrant integration
    - vagrant up from the root folder
    - provisioning step will install the app and run it in the background

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
    - [flyway](http://flywaydb.org/) database migrations
        - ./gradlew flywayMigrate
        - Incremental database design
    - [protractor](https://github.com/angular/protractor) functional tests
        - ./gradlew npm (installs node dependencies)
        - ./gradlew protractorRun (runs functional tests)
            - tests defined in src/test/javascript/e2e/scenarios.js
            - leverages protractor support for AngularJS
    - javascript best practices built in
        - [jshint](http://jshint.com/)
        - combination
        - minification
        - source map
        - [karma](http://karma-runner.github.io/) javascript unit testing
            - ./gradlew karmaRun
    - auto builds git.properties and packages it in your application
        - contains the working branch and commit id
        - spring actuator makes this information available at the /info endpoint

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
    - Password hashing using [jBCrypt](http://www.mindrot.org/projects/jBCrypt/)
        - Java implementation of OpenBSD's Blowfish
        - Designed to raise the cost of off-line password cracking and
          frustrate fast hardware implementation

- JSR 303 Validation
    - Validation groups
    - Validation at the Controller level
    - Validation at the Entity level
    - Validation of External Configuration
    - Strong password validator via [vt-password](http://code.google.com/p/vt-middleware/wiki/vtpassword)

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

### Running the application with vagrant ###

Prerequisites

- [Vagrant](https://www.vagrantup.com/)

Install the app and run in the background (takes a while the first time)

    vagrant up

The app will be provisioned and started in the background

It may take a minute or two before you are able to reach http://localhost:8080 

### Running the application with docker ###

Prerequisites

- [Docker](https://www.docker.io/)

Note: this assumes your docker binary is **docker.io**

Build the docker image (rerun this after making any changes to your app)

    sudo docker.io build -t spring .

Run the docker image with port mapping

    sudo docker.io run -p 80:80 -i -t spring

If everything goes well you should eventually see a message like the following in your console:

    Started Application in 14.034 seconds (JVM running for 14.538)

You should now be able to access the application on your host system by visiting http://localhost

If you would like to save the container you created as a tar file and reuse it:

    sudo docker.io ps -a
    // note the container id for the tag spring:latest
    sudo docker.io export 8bf522a7ee22 > spring.tar
    // where 8bf522a7ee22 is the container id noted in the previous command

You can then restore the result tar into docker using the following command

    cat spring.tar | sudo docker.io import - spring:new

If you would like to remove all Docker related assets you can use the following commands:

    sudo docker.io rm `sudo docker.io ps -a -q`
    sudo docker.io rmi `sudo docker.io images -q`

#### Running the application without docker ####

Prerequisites

- [Java 7](http://www.oracle.com/technetwork/java/javase/overview/index.html)
- [Apache ActiveMQ](http://activemq.apache.org/)
- [Postgresql](http://www.postgresql.org/) Database (empty database named 'spring')
- [nodejs] (http://nodejs.org/)

Start ActiveMQ (from activeMQ HOME)

    bin/activemq start

Create database tables via flyway (from project dir)

    ./gradlew flywayMigrate

Build the application

    ./gradlew build

Run the Web App on port 8080 (from project dir)

    ./gradlew bootRun

#### Verify the Application ####

By default the system will have 1 admin user with credentials (admin/admin).

You can login at http://localhost:8080 or simply http://localhost if using Docker.

You can register new users by clicking the Register link before login.

#### Running the functional tests ####

By default the chrome web browser is used.  Update protractor-conf.js to change this.

Install node dependencies (do once and then anytime package.json is updated)

    ./gradlew npm

Start the app if not already started

    ./gradlew bootRun

Run protractor (update baseUrl in protractor-conf.js to http://localhost if running with Docker)

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

If you are using Spring Tool Suite or Eclipse, you need to run

    ./gradlew eclipseWtp

to reconfigure your project so you can drag the app directly to tc Server.

#### Unit Testing the Angular app ####

To start Karma in watch mode simply run the following:

    ./gradlew karmaRun

You can simply edit your javascript files and the tests will be re-run on modification

#### Development Mode ####

Flip a switch in application.properties in order to run in development mode

    info.app.development=true

This change switches from using the minified version of the app javascript to the un-minified

This change also switches to serving static assets from the project source
directory instead of from the packaged jar file so that you may reload
the browser to see your changes immediately

You may also want to turn off the thymeleaf cache in application.properties during development

    spring.thymeleaf.cache=false

And also turn off the static resource cache

    spring.resources.cachePeriod=0

#### More customization options ####

See [Spring Boot Reference Guide](http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/)

#### Notes on the Docker support ####

The docker config by default sets up Postgresql, ActiveMQ, and Varnish to support your application.  When moving
to a production deployment you can leverage
[AWS Elastic Beanstalk for Docker](http://aws.typepad.com/aws/2014/04/aws-elastic-beanstalk-for-docker.html),
with minimal changes to your application.

The documentation on how to do this should be pretty straigtforward.  You basically upload a zip file containing
your entire project.  Since it contains a Dockerfile, AWS EB will detect this and setup your application, with
all the additional benefits of AWS EB such as provisioning, monitoring, scaling, and load balancing.

One change that you will probably want to make is to use a database external to your Docker image.  You can
use Amazon RDS, for example, to create a Postgresql database.  The only change you would need to make is to update
**application.properties** and **jooq-config.xml** with the connection settings for your RDS database before
uploading your .zip file.

Optionally, you can also remove the steps in Dockerfile and the config/docker directory that deal with
setting up Postgresql (since it will be managed separately).

#### License ####
Wellington is released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0).



