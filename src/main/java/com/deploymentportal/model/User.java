package com.deploymentportal.model;

// JPA annotations for database entity mapping
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * User Entity - Represents a user in the deployment portal system
 * 
 * This class defines the structure of user data stored in the database.
 * Users have different roles that determine their permissions:
 * - superadmin: Full system access, can manage users
 * - admin: Can manage deployments and create releases
 * - developer: Can create deployment requests and mark them production ready
 * 
 * The username serves as the primary key, so each username must be unique.
 */
@Entity // Marks this class as a JPA entity (database table)
@Table(name = "users") // Specifies the database table name
public class User {
    
    // Primary key field - username must be unique across all users
    @Id // Marks this field as the primary key
    private String username;
    
    // Password field - stored as plain text (in production, should be encrypted)
    private String password;
    
    // Role field - determines user permissions in the system
    private String role;

    /**
     * Getter method for username
     * @return The username of this user
     */
    public String getUsername() { 
        return username; 
    }
    
    /**
     * Setter method for username
     * @param username The username to set for this user
     */
    public void setUsername(String username) { 
        this.username = username; 
    }
    
    /**
     * Getter method for password
     * @return The password of this user
     */
    public String getPassword() { 
        return password; 
    }
    
    /**
     * Setter method for password
     * @param password The password to set for this user
     */
    public void setPassword(String password) { 
        this.password = password; 
    }
    
    /**
     * Getter method for role with backward compatibility
     * Maps legacy "user" role to "developer" for consistency
     * @return The role of this user (superadmin, admin, or developer)
     */
    public String getRole() {
        // Handle legacy "user" role by converting it to "developer"
        if ("user".equalsIgnoreCase(role)) {
            return "developer";
        }
        return role;
    }
    
    /**
     * Setter method for role with backward compatibility
     * Maps legacy "user" role to "developer" for consistency
     * @param role The role to set for this user
     */
    public void setRole(String role) {
        // Handle legacy "user" role by converting it to "developer"
        if ("user".equalsIgnoreCase(role)) {
            this.role = "developer";
        } else {
            this.role = role;
        }
    }
}