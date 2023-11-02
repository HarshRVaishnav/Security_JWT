# Spring Boot 2.0 Security with JWT Implementation
This project demonstrates the implementation of security using Spring Boot 2.7.12 and JSON Web Tokens (JWT). It includes the following features:


## Features
* User registration and login with JWT authentication
* Password encryption using BCrypt
* Role-based authorization with Spring Security
* Customized access denied handling

## Technologies
* Spring Boot 2.0
* Spring Security
* JSON Web Tokens (JWT)
* BCrypt
* Maven

## Getting Started
To get started with this project, you will need to have the following installed on your local machine:

* JDK 11+
* Maven 3+

To build and run the project, follow these steps:

* Clone the repository: `git clone https://github.com/vikrantcropdata/Json-Web-Token-Springboot/tree/fb-jwt-working`
* Navigate to the project directory: cd Json-Web-Token-Springboot
* Add database "jwt_auth_db" to mysql
* Build the project: mvn clean install

## Steps to run sample

```
    # Run SpringBoot-Jwt-Application SpringBoot Application 
    mvn spring-boot:run
    
    # Start docker containers 
    docker-compose up -d
```

## Application contains 3 modules:

    1) customer         
    2) order            
        1) orderDetails 
    3) product          

-> The application will be available at http://localhost:8080.
## Documentation (Swagger)
Visit it [Swagger Application Document](http://localhost:8080/swagger-ui.html) to visualize the exposed API endpoints.

![RestApi.png](src%2Fmain%2Fresources%2Fstatic%2FRestApi.png)
