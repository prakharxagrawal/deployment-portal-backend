package com.deploymentportal;

// Spring Boot framework imports for application startup
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Main Application Class - Entry point for the Deployment Portal Spring Boot application
 * 
 * This class serves as the starting point for the entire backend application.
 * When this application starts, it will:
 * 1. Initialize the Spring Boot context
 * 2. Scan for all components, services, repositories, and controllers
 * 3. Set up the embedded H2 database
 * 4. Load sample data from data.sql
 * 5. Start the embedded Tomcat server on port 8080
 */
@SpringBootApplication // This annotation enables auto-configuration and component scanning
@EntityScan("com.deploymentportal.model") // Tells Spring where to find JPA entity classes
public class Application {
    
    /**
     * Main method - Entry point of the Java application
     * 
     * @param args Command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        // Start the Spring Boot application
        // This will initialize the entire application context and start the web server
        SpringApplication.run(Application.class, args);
    }
}