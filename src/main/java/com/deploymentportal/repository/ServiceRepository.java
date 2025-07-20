package com.deploymentportal.repository;

// Import the Service entity model
import com.deploymentportal.model.Service;

// Spring Data JPA repository interface
import org.springframework.data.jpa.repository.JpaRepository;

// Spring annotation for repository beans
import org.springframework.stereotype.Repository;

// Java collections for return types
import java.util.List;

/**
 * ServiceRepository - Data access layer for Service entities
 * 
 * This repository provides database operations for managing the catalog of services
 * that can be deployed. Services are reference data used when creating deployment requests.
 * 
 * The generic parameters are:
 * - Service: The entity type this repository manages
 * - Long: The type of the primary key (auto-generated ID)
 */
@Repository // Spring annotation to mark this as a repository component
public interface ServiceRepository extends JpaRepository<Service, Long> {
    
    /**
     * Find services by name containing a search term (case-insensitive)
     * 
     * This method uses Spring Data JPA query derivation:
     * - findBy: Start of query method
     * - Name: Property to search in
     * - Containing: SQL LIKE operation with wildcards
     * - IgnoreCase: Makes the search case-insensitive
     * 
     * Generated SQL: SELECT * FROM services WHERE LOWER(name) LIKE LOWER('%searchTerm%')
     * 
     * @param name The search term to look for in service names
     * @return List of services whose names contain the search term
     */
    List<Service> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find all services ordered by name in ascending order
     * 
     * This method uses Spring Data JPA query derivation:
     * - findAll: Get all records
     * - By: Separator (no conditions in this case)
     * - OrderBy: Specify sorting
     * - Name: Property to sort by
     * - Asc: Ascending order
     * 
     * Generated SQL: SELECT * FROM services ORDER BY name ASC
     * 
     * @return List of all services sorted alphabetically by name
     */
    List<Service> findAllByOrderByNameAsc();
}
