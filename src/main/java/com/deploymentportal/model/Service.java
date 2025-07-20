package com.deploymentportal.model;

// JPA annotations for database entity mapping
import jakarta.persistence.*;

/**
 * Service Entity - Represents a service that can be deployed
 * 
 * This class defines the catalog of available services in the system.
 * Services follow a naming convention of aaa-bbb-ccc format (e.g., auth-login-service).
 * 
 * Services are used as reference data when creating deployment requests.
 * Each service has a unique name and an auto-generated ID.
 */
@Entity // Marks this class as a JPA entity (database table)
@Table(name = "services") // Specifies the database table name
public class Service {
    
    // Primary key - auto-generated ID for each service
    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID generation
    private Long id;
    
    // Service name - must be unique across all services
    @Column(unique = true, nullable = false) // Database constraints: unique and not null
    private String name;
    
    /**
     * Default constructor required by JPA
     * Creates an empty Service object
     */
    public Service() {}
    
    /**
     * Constructor with service name
     * Creates a Service object with the specified name
     * 
     * @param name The name of the service (e.g., "auth-login-service")
     */
    public Service(String name) {
        this.name = name;
    }
    
    /**
     * Getter method for service ID
     * @return The auto-generated ID of this service
     */
    public Long getId() { 
        return id; 
    }
    
    /**
     * Setter method for service ID
     * Note: Usually not called directly as ID is auto-generated
     * @param id The ID to set for this service
     */
    public void setId(Long id) { 
        this.id = id; 
    }
    
    /**
     * Getter method for service name
     * @return The name of this service
     */
    public String getName() { 
        return name; 
    }
    
    /**
     * Setter method for service name
     * @param name The name to set for this service (should follow aaa-bbb-ccc format)
     */
    public void setName(String name) { 
        this.name = name; 
    }
}