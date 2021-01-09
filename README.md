![Java CI with Maven](https://github.com/karthikaiselvan/account-transaction-service/workflows/Java%20CI%20with%20Maven/badge.svg?branch=main) ![Docker](https://github.com/karthikaiselvan/account-transaction-service/workflows/Docker/badge.svg?branch=main)

# Microservice: account-transaction-service
This is a demo microservice for account transaction as part of mobile challengers banking - coding interview

### Tech stack used:
1. Java 11
2. Spring Boot 2.4.1
3. Spring REST API (Spring Web)
4. Lombok 1.18.16
5. Spring Data JPA & H2 in-memory database (exposed the client dashboard to see the records)
6. springdoc-openapi-ui - Auto generated API documentation using OpenAPI 3.0 and exposed Swagger UI for the same (Refer the link at the bottom of this page)
7. Dockerized (Dockerfile is included and used the latest concept called multi-layered image building for optimized build time)
8. TDD approach using JUnit 5, Mockito, and Spring Boot Test
9. JaCoCo for code coverage

### JaCoCo code coverage is: ``` 100 % ```
![alt text](https://github.com/karthikaiselvan/account-transaction-service/blob/main/JaCoCo_Report.jpg?raw=true)

### Steps to build the Docker Image:
##### Step 1: Enter the project directory, 
    cd <project_directory>

##### Step 2: To clean and package the micro-service locally
    mvn clean package
 
##### Step 3: Build the docker image and store it in a local repository (Dockerfile is there in the root of the directory itself)
    docker build . --tag account-transaction-service

##### Step 4: Run the micro-service as a container in the docker:
    docker run -it -p 8080:8080 account-transaction-service:latest

Note: I have used multi-layered approach in docker and spring.

### OpenAPI 3.0 Documentation URL: 
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config

Note: Also [Postman Collection](https://github.com/karthikaiselvan/account-transaction-service/blob/main/account-transaction-service.postman_collection.json) has been pushed into the repository for your reference.

### In-Memory Database Dashboard:
http://localhost:8080/h2-console/login.jsp   

Note: Username and Password for the in-memory H2 database can be set differently by passing environment variables namely ```DB_USER``` and ```DB_PWD``` if required, else H2 db provided default username and password will be used. For more details please refer: [application.yml](https://github.com/karthikaiselvan/account-transaction-service/blob/main/src/main/resources/application.yml#L3)
