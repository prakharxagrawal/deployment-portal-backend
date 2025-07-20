package com.deploymentportal.repository;

// Import the Deployment entity model
import com.deploymentportal.model.Deployment;

// Spring Data JPA repository interface
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DeploymentRepository - Data access layer for Deployment entities
 * 
 * This repository manages the core deployment request data. It provides all
 * standard CRUD operations through JpaRepository and can be extended with
 * custom query methods as needed.
 * 
 * The deployment controller uses this repository along with streams and filters
 * to handle complex search and filtering operations in memory for optimal performance
 * with the current dataset size.
 * 
 * The generic parameters are:
 * - Deployment: The entity type this repository manages
 * - Long: The type of the primary key (auto-generated ID)
 * 
 * Available inherited methods include:
 * - findAll(): Get all deployments
 * - findById(Long id): Get deployment by ID
 * - save(Deployment): Save or update a deployment
 * - delete(Deployment): Delete a deployment
 * - count(): Count total deployments
 */
public interface DeploymentRepository extends JpaRepository<Deployment, Long> {
    // No custom query methods needed currently
    // All filtering and searching is handled in the controller layer
    // using Java streams for flexibility and performance
}