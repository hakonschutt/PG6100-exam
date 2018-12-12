[![Build Status](https://travis-ci.com/hakonschutt/PG6100-exam.svg?token=685Vkj7Z4Bw9G4suxzq5&branch=master)](https://travis-ci.com/hakonschutt/PG6100-exam)

# Cinema application - Enterprise exam (PG6100)

## About the application (What it does)

- Short pitch about applications

[Git repo](https://github.com/hakonschutt/PG6100-exam)

## How to run

The application relies on docker to start the entire cluster. Make sure to have [java](https://www.java.com/en/download/), [maven](https://maven.apache.org/download.cgi) and [docker](https://www.docker.com/get-started) installed before executing the following commands

```bash
$ mvn clean install
$ docker-compose up --build
```
if you are on a linux system you will have to use ```sudo``` as prefix for both commands

## How to test

- How to test the project
- If you have special login for users (eg, an admin), write down login/password, so it can be used

## Structure (Microservice architecture)
```
exam-
    | - backend
    |   |--- booking
    |   |--- events (GraphQl)
    |   |--- venues
    |   |--- movies
    |   |--- payment
    |   |--- gateway
    |   |--- eureka
    |   |--- template
    |   |--- dtos
    |   |--- utils
    | - frontend
    |   |--- public
    |   |--- src
    |   |--- nginx
    | - report 
    |   |---pom.xml (only, reports gets aggregated here)
  
```
## Implementation

The marking will be strongly influenced by the quality and quantity of features you can implement. For each main feature, you MUST have an end-to-end test to show it, and also you need to discuss it in the documentation. A feature without tests and documentation is like a feature that does not exist. For B/A grades, the more features you implement and test in your project, the better.

## Technologies

Which different technologies you did choose to use

## Developers
- ##### [Jarand Waage Kleppa](https://github.com/kleppa)
- ##### [Håkon Schutt](https://github.com/hakonschutt)
- ##### [Bjørn Olav Salvesen](https://github.com/bjornosal)

For each student, a brief description of your individual contributions to the project. Also make
sure to specify your Git user-names so examiners can verify what you wrote based on the Git history. Furthermore, make sure that, for each student, you specify which is the main REST service s/he is responsible for.

## Issue

- Devopsuser
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
