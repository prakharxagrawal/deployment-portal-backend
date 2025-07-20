package com.deploymentportal.repository;

// Import the Release entity model
import com.deploymentportal.model.Release;

// Spring Data JPA repository interface and query annotations
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Java collections for return types
import java.util.List;

/**
 * ReleaseRepository - Data access layer for Release entities
 * 
 * This repository manages release data used for organizing deployments.
 * Releases follow YYYY-MM format and are displayed in reverse chronological order
 * (newest first) throughout the application.
 * 
 * The generic parameters are:
 * - Release: The entity type this repository manages
 * - Long: The type of the primary key (auto-generated ID)
 */
public interface ReleaseRepository extends JpaRepository<Release, Long> {
    
    /**
     * Find a release by its name
     * 
     * Uses JPQL (Java Persistence Query Language) for explicit query definition.
     * This is useful when the method name would be too long or complex for
     * Spring Data JPA's automatic query derivation.
     * 
     * @Query: Custom JPQL query
     * @Param: Maps method parameter to query parameter
     * 
     * @param name The release name to search for (e.g., "2025-01")
     * @return The release with the specified name, or null if not found
     */
    @Query("SELECT r FROM Release r WHERE r.name = :name")
    Release findByName(@Param("name") String name);
    
    /**
     * Find all releases ordered by name in descending order (newest first)
     * 
     * Uses JPQL to ensure releases are sorted properly. Since release names
     * follow YYYY-MM format, sorting by name DESC gives us the newest releases first.
     * 
     * Examples of sorting:
     * - 2025-12 (December 2025)
     * - 2025-11 (November 2025)
     * - 2025-01 (January 2025)
     * 
     * @return List of all releases sorted with newest first
     */
    @Query("SELECT r FROM Release r ORDER BY r.name DESC")
    List<Release> findAllOrderByNameDesc();
}