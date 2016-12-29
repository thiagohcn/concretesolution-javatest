Instructions
==================

1) To compile and run the application:

	./gradlew bootRun


_______________


General information
------------------

* This Application uses Spring framework
* It Runs on Spring Boot, uses Spring Rest and Spring Security
* I did not implement test. I started to configure Mockito, but I have little experience with tests.
* It was my first time configuring an application with Gradle, I'm more familiar with Maven.

_______________


Restful service 
==================

##### Table describing the restful services:

| Url                                                   | HTTP Method | Description                                                                                                 |
|-------------------------------------------------------|-------------|-------------------------------------------------------------------------------------------------------------|
| https://stark-stream-12556.herokuapp.com/user/create  | POST        | Create a User. The json format follows the test description                                                 |
| https://stark-stream-12556.herokuapp.com/user/login   | POST        | Login a user. The json must be like: { "email": "joao@silva.org", "password": "hunter2" }                   |
| https://stark-stream-12556.herokuapp.com/user/1       | GET         | It number on the path is the id of the user. You must pass the valid token through Authorization header     |



