package com.deploymentportal.config;

// Spring Framework imports for web configuration
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web Configuration for the Deployment Portal
 * 
 * This configuration class handles Cross-Origin Resource Sharing (CORS) settings
 * to allow the Angular frontend (running on port 4200) to communicate with the
 * Spring Boot backend (running on port 8080).
 * 
 * CORS is necessary because browsers block requests between different origins
 * (different protocols, domains, or ports) for security reasons.
 * 
 * @author Deployment Portal Team
 * @version 1.0
 */
@Configuration // Indicates this class contains Spring configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure CORS mappings for all endpoints
     * 
     * This method sets up CORS rules that allow:
     * - Angular frontend to make requests to Spring Boot backend
     * - Session cookies to be sent with requests (allowCredentials)
     * - All HTTP methods (GET, POST, PUT, DELETE, OPTIONS)
     * - All headers to be included in requests
     * 
     * @param registry CORS registry to configure mappings
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**") // Apply CORS to all endpoints (/** means all paths)
                .allowedOrigins("http://localhost:4200") // Allow requests from Angular dev server
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow all necessary HTTP methods
                .allowedHeaders("*") // Allow all headers (including custom headers)
                .allowCredentials(true) // CRITICAL: Allow session cookies to be sent
                .maxAge(3600); // Cache preflight requests for 1 hour (3600 seconds)
    }
}
