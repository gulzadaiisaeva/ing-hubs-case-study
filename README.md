# Stock Project for ING Hubs Türkiye Case Study

## Overview

This project is implemented using Java 17 and Spring Boot. It provides a stock management system as part of the ING Hubs Türkiye case study. The project also includes Docker configuration to facilitate containerization.

## Getting Started

### Prerequisites

- Java 17
- Docker (for containerization)

### Running the Application
Clone the repository

```bash
git clone https://github.com/gulzadaiisaeva/ing-hubs-case-study.git
```

Navigate to the `ing-hubs-case-study` folder
```bash
cd ing-hubs-case-study
```

Build the JAR File

 ```bash
   mvn clean install
```

This command uses Maven to clean and package the application into a JAR file, which will be placed in the target directory.

## Run Locally

To run the application locally, use the following command:
   ```bash
   java -jar target/case-study.jar
```

By default, the application will start on port 8080. You can change this by modifying the application.properties file.

## Running the Application with Docker

To build the Docker image, run the following command in the root folder of the project:

   ```bash
   docker build -t case-study:latest . 
   ```

To run the Docker container, use:
  ```bash
   docker run -d -p 8080:8080 case-study:latest
   ```

This command maps port 8080 of the Docker container to port 8080 on your local machine.

### Postman Collection

A Postman collection is included in the root folder of the repository. You can use it to test the API endpoints provided by the application. The collection file is [case-study.postman_collection.json](case-study.postman_collection.json)

## Api Documentation

After running the project the API documentation will be available at the following URL:
[Swagger](http://localhost:8080/case-study/swagger-ui/index.html)

### Contact
**iisaevagulzada@gmail.com**
  