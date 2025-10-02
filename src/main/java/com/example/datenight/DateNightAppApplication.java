package com.example.datenight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DateNightAppApplication {
    //by convention, Spring Boot apps have a class named *application
    public static void main(String[] args) {
        SpringApplication.run(DateNightAppApplication.class, args);
        /*this is the entry point of the Java application, which
        1) Starts the Spring application context (all components are initialized)
        2) Starts the embedded server (Tomcat by default) to make API accessible via HTTP
        3) Sets up auto-configuration, including your REST controllers and JSON support
        4) Begins listening on the default port of 9090 for incoming requests*/

    }
}
