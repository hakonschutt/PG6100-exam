[![Build Status](https://travis-ci.com/hakonschutt/PG6100-exam.svg?token=685Vkj7Z4Bw9G4suxzq5&branch=master)](https://travis-ci.com/hakonschutt/PG6100-exam)

# Cinema application - Enterprise exam (PG6100)

[GitHub Repository](https://github.com/hakonschutt/PG6100-exam)
## About the application (What it does)

- Short pitch about applications

We have created a system for cinemas. This system lets users browse different movies available at the cinema venues in this system
that is coming the next 7 days. 
This will also let admins create venues, movies,



## How to run

The application relies on docker to start the entire cluster. Make sure to have [java](https://www.java.com/en/download/), [maven](https://maven.apache.org/download.cgi) and [docker](https://www.docker.com/get-started) installed before executing the following commands

```bash
$ mvn clean install
$ docker-compose up --build
```
Up time depends on hardware (our case aprox. 2 mins)

if you are on a linux OS you will have to use `bash sudo` as prefix for both commands

## How to test

- How to test the project
- If you have special login for users (eg, an admin), write down login/password, so it can be used
 admin1
 password/
## Structure (Microservice architecture)

```
exam-
    | - backend
    |   |--- booking (amqp receiver)
    |   |--- events (GraphQl, Circuit-breaker)
    |   |--- venues (is the service that is launched twice)
    |   |--- movies
    |   |--- payment (amqp sender)
    |   |--- auth
    |   |--- gateway(Spring cloud gateway)
    |   |--- eureka (Service Discovery)
    |   |--- dtos
    |   |--- utils
    | - frontend
    |   |--- public
    |   |--- src
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
 `[PP][user1&&user2]` - means that the commit has been pair programmed between those users regardless of the commit owner in git history.
 

## Developers
- ##### [Bjørn Olav Salvesen](https://github.com/bjornosal)
solely responsible for Movies api :

The movie api is responsible for providing and creating data about different movies.
 This api allows you to create cinema movies, retrieve movies.
 
 contributed also to:
 - auth
 - payment
 - gateway
 - eureka
 - utils
 - movies
- ##### [Jarand Waage Kleppa](https://github.com/kleppa)
solely responsible for Venues api:

The venue api is responsible for providing and creating data about different cinemas.
 This api allows you to create cinema venues, create rooms inside venues, retrieve venues 
 
 contributed also to:
 - events
 - auth
 - gateway
 - eureka
 - utils
 - venues
- ##### [Håkon Schutt](https://github.com/hakonschutt)
solely responsible for Booking api:

The booking api is responsible for providing and creating data about different booking.
 This api allows you to create cinema booking, retrieve bookings, tickets.
 
 contributed also to:
 - events
 - gateway
 - eureka
 - frontend 
 - utils
 - booking
 
 The apis above has been developed individually by the different group members.
For each student, a brief description of your individual contributions to the project. Also make
sure to specify your Git user-names so examiners can verify what you wrote based on the Git history. Furthermore, make sure that, for each student, you specify which is the main REST service s/he is responsible for.

## Issue
##### Gateway / Service Discovery
   
   We experienced massive issues with gateway / service discovery. Seems like spring cloud gateway and eureka Service Discovery
    is extremly sensitive to what dependecies it inherits. e.g spring-boot-starter-web CANNOT be in gateway. This one was easy to find, but we didnt manage to find out why it managed
    to register the service to the service discovery and only return.
     
    http 1.1 GET 200 ok thunked response
     
   and nothing from the actuall endpoint we were routing to. Neither the gateway or the service discovery logged out any activity when we did these calls, but we received payloads back
   This resulted in us having to resolve to rebuild the entire pom structure. After doing this it finally worked.
    
##### git: Devopsuser 
 Devopsuser is hakonschutt. When we had devops exam we had to use anonymous git users and he forgot to switch the profile name and email in his git config on his computer. Thats why there is 6 commits from this user

#### Jacoco:Graphql
 Jacoco does not manage to generate code coverage for events microservice,  because it uses GraphQl.

# TODO BEFORE DELIVERY (DELETE ME BEFORE DELIVERY)
- ### DELETE TEMPLATE
- ### CHECK PORTS
- ### AUTH IN GATEWAY
- ### E2E TESTS
- ### CLEAN UP STUFF
- ### DELETE NGINX
- ### DELETE CREATEREACTAPP DOC
- ### MAYBE DELETE NGINX IN FRONTEND
- ### ADD ALL SERVICES TO REPORT
- ### REMOVE TODOS
- ### CHECK TESTS AND CODE COVERAGE
- ### CHECK LOCALAPPRUNNER 
- ### AMQP
