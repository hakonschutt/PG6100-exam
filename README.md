[![Build Status](https://travis-ci.com/hakonschutt/PG6100-exam.svg?token=685Vkj7Z4Bw9G4suxzq5&branch=master)](https://travis-ci.com/hakonschutt/PG6100-exam)

# Cinema application - Enterprise exam (PG6100)

## About the application (What it does)

- Short pitch about applications
We have created a system for cinemas.
[Git repo](https://github.com/hakonschutt/PG6100-exam)

## How to run

The application relies on docker to start the entire cluster. Make sure to have [java](https://www.java.com/en/download/), [maven](https://maven.apache.org/download.cgi) and [docker](https://www.docker.com/get-started) installed before executing the following commands

```bash
$ mvn clean install
$ docker-compose up --build
```
Up time depends on hardware (our case aprox. 2 mins)

if you are on a linux system you will have to use ```bash sudo``` as prefix for both commands

## How to test

- How to test the project
- If you have special login for users (eg, an admin), write down login/password, so it can be used

## Structure (Microservice architecture)

```
exam-
    | - backend
    |   |--- booking (amqp receiver)
    |   |--- events (GraphQl)
    |   |--- venues (is the service that is launched twice)
    |   |--- movies
    |   |--- payment (amqp sender)
    |   |--- gateway
    |   |--- eureka (Service Discovery)
    |   |--- dtos
    |   |--- utils
    | - frontend
    |   |--- public
    |   |--- src
    |   |--- nginx
    | - report 
    |   |---pom.xml (only, code-coverage reports gets aggregated here after mvn test is executed)
  
```
## Implementation

The marking will be strongly influenced by the quality and quantity of features you can implement. For each main feature, you MUST have an end-to-end test to show it, and also you need to discuss it in the documentation. A feature without tests and documentation is like a feature that does not exist. For B/A grades, the more features you implement and test in your project, the better.

## Technologies

Which different technologies you did choose to use

For our project we used the Spring/Spring Boot framework, React and Kotlin. 
In addition we also used some other libraries for production and testing.
For production we used Docker, Docker-compose and a lot of the Netflix-stack( Hystrix ,Eureka, Ribbon), in addition to Redis, RabbitMQ,  and Postgres databases.

Features :
 - amqp
 - graphql - events directory
 - payment - amqp microservice
 - Maybe frontend nginx?
 - React 
 - Redux
 - client side load balancing
 - fanout exchange
 
 ## Workflow
 
 ### Git 
 
 Git has been heavily used. We started the project locking down the master branch and requiering other group members to overlook the commit before pulling the change into master.
 
 ### Travis
 
 We are also building and testing the new pull-requests with travis before pulling them into master.
 [PP][user1&&user2] - means that the commit has been pair programmed
 

## Developers
- ##### [Jarand Waage Kleppa](https://github.com/kleppa)
Venues api:

The venue api is responsible for providing and creating data about different cinemas.
 This api allows you to create cinema venues, create rooms inside venues, retrieve venues, retrieve rooms
 
- ##### [Håkon Schutt](https://github.com/hakonschutt)
Booking api:

The booking api is responsible for providing and creating data about different booking.
 This api allows you to create cinema booking, retrieve booking.
 
- ##### [Bjørn Olav Salvesen](https://github.com/bjornosal)
Movies api :

The movie api is responsible for providing and creating data about different movies.
 This api allows you to create cinema movies, retrieve movies.



 The apis above has been developed individually by the different group members.
For each student, a brief description of your individual contributions to the project. Also make
sure to specify your Git user-names so examiners can verify what you wrote based on the Git history. Furthermore, make sure that, for each student, you specify which is the main REST service s/he is responsible for.

## Issue

- Devopsuser - is hakonschutt. When we had devops exam we had to use anonymous git users and he forgot to switch. thats why there is 6 commits from this user
- events code coverage is fucked because of graphql

# TODO BEFORE DELIVERY (DELETE ME BEFORE DELIVERY)
- ### DELETE TEMPLATE
- ### CHECK PORTS
- ### E2E TESTS
- ### CLEAN UP STUFF
- ### DELETE NGINX
- ### DELETE CREATEREACTAPP DOC
- ### MAYBE DELETE NGINX IN FRONTEND
- ### ADD ALL SERVICES TO REPORT
- ### REMOVE TODOS
- ### CHECK TESTS AND CODE COVERAGE
