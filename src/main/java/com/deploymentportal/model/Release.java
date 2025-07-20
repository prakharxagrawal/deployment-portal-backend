package com.deploymentportal.model;

// JPA annotations for database entity mapping
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

// Validation annotations for data integrity
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Release Entity - Represents a release in the deployment portal system
 * 
 * This class defines the structure of release data used for organizing deployments.
 * Releases follow a strict YYYY-MM naming format (e.g., 2025-01, 2025-12) to ensure
 * consistent chronological organization.
 * 
 * Only Admin and Super Admin users can create new releases through the UI.
 * Each release has a unique name and a descriptive text explaining its purpose.
 */
@Entity // Marks this class as a JPA entity (database table)
@Table(name = "releases") // Specifies the database table name
public class Release {
    
    // Primary key - auto-generated ID for each release
    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID generation
    private Long id;
    
    // Release name - must follow YYYY-MM format and be unique
    @NotBlank(message = "Release name is required") // Validation: cannot be empty
    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "Release name must be in YYYY-MM format") // Regex validation
    @Column(unique = true) // Database constraint: must be unique
    private String name;
    
    // Release description - provides context about the release
    @NotBlank(message = "Description is required") // Validation: cannot be empty
    private String description;

    /**
     * Default constructor required by JPA
     * Creates an empty Release object
     */
    public Release() {}
    
    /**
     * Constructor with release details
     * Creates a Release object with the specified name and description
     * 
     * @param name The release name in YYYY-MM format (e.g., "2025-01")
     * @param description A descriptive text about the release purpose
     */
    public Release(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Getter method for release ID
     * @return The auto-generated ID of this release
     */
    public Long getId() { 
        return id; 
    }
    
    /**
     * Setter method for release ID
     * Note: Usually not called directly as ID is auto-generated
     * @param id The ID to set for this release
     */
    public void setId(Long id) { 
        this.id = id; 
    }
    
    /**
     * Getter method for release name
     * @return The name of this release in YYYY-MM format
     */
    public String getName() { 
        return name; 
    }
    
    /**
     * Setter method for release name
     * @param name The name to set for this release (must follow YYYY-MM format)
     */
    public void setName(String name) { 
        this.name = name; 
    }
    
    /**
     * Getter method for release description
     * @return The description of this release
     */
    public String getDescription() { 
        return description; 
    }
    
    /**
     * Setter method for release description
     * @param description The description to set for this release
     */
    public void setDescription(String description) { 
        this.description = description; 
    }
}