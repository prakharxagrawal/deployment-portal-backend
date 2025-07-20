package com.deploymentportal.repository;

// Import the User entity model
import com.deploymentportal.model.User;

// Spring Data JPA repository interface
import org.springframework.data.jpa.repository.JpaRepository;

// Java Optional for null-safe operations
import java.util.Optional;

/**
 * UserRepository - Data access layer for User entities
 * 
 * This interface extends JpaRepository to provide CRUD operations for User entities.
 * Spring Data JPA automatically implements this interface at runtime, providing
 * standard database operations like save, findAll, findById, delete, etc.
 * 
 * The generic parameters are:
 * - User: The entity type this repository manages
 * - String: The type of the primary key (username in this case)
 * 
 * Custom query methods are defined here following Spring Data JPA naming conventions.
 */
public interface UserRepository extends JpaRepository<User, String> {
    
    /**
     * Find a user by their username
     * 
     * This method follows Spring Data JPA naming convention: findBy + PropertyName
     * Spring automatically generates the SQL query: SELECT * FROM users WHERE username = ?
     * 
     * @param username The username to search for
     * @return Optional<User> - Contains the user if found, empty if not found
     *         Using Optional prevents null pointer exceptions and makes the code more robust
     */
    Optional<User> findByUsername(String username);
}