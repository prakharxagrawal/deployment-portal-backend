package com.deploymentportal.model;

// JPA annotations for database entity mapping and lifecycle callbacks
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

// Java collections for storing multiple environments
import java.util.List;

/**
 * Deployment Entity - Represents a deployment request in the system
 * 
 * This is the core entity of the deployment portal. It contains all information
 * about a deployment request including:
 * - Basic request information (service, team, release)
 * - Environment targeting (UAT, PERF, PROD)
 * - RLM ID tracking for each environment
 * - Status progression (Open → In Progress → Pending → Completed)
 * - Production readiness approval
 * - Audit trail (dates, creator)
 * 
 * The entity uses JPA lifecycle callbacks to automatically manage timestamps.
 */
@Entity // Marks this class as a JPA entity (database table)
public class Deployment {
    
    // Primary key - auto-generated ID for each deployment
    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID generation
    private Long id;
    
    // Unique identifier for tracking deployments (format: MSDR0000001)
    private String serialNumber;
    
    // Customer Service Identifier - used for business tracking
    private String csiId;
    
    // Service name being deployed (e.g., "auth-login-service")
    private String service;
    
    // Business request identifier (e.g., "jenkins-REQ001")
    private String requestId;
    
    // Flag indicating if this is a configuration request (vs application deployment)
    private Boolean isConfig = false;
    
    // Configuration-specific request ID (only used when isConfig = true)
    private String configRequestId;
    
    // List of environments requested for deployment (UAT1, UAT2, UAT3, PERF, PROD)
    @ElementCollection // JPA annotation to store collection in separate table
    private List<String> environments;

    // Git release branch name (for development workflow)
    private String releaseBranch;
    
    // Development team responsible for this deployment
    private String team;
    
    // Timestamp when the request was created
    private java.time.LocalDateTime dateRequested;
    
    // Timestamp when the request was last modified
    private java.time.LocalDateTime dateModified;
    
    // Release name in YYYY-MM format (e.g., "2025-01")
    private String release;
    
    // Current status of the deployment (Open, In Progress, Pending, Completed)
    private String status = "Open"; // Default status for new requests
    
    // Username of the person who created this request
    private String createdBy;
    
    // RLM (Release Management) IDs for User Acceptance Testing environments
    private String rlmIdUat1;  // UAT1 environment RLM ID
    private String rlmIdUat2;  // UAT2 environment RLM ID
    private String rlmIdUat3;  // UAT3 environment RLM ID
    
    // RLM IDs for Performance Testing environments
    private String rlmIdPerf1; // PERF1 environment RLM ID
    private String rlmIdPerf2; // PERF2 environment RLM ID
    
    // RLM IDs for Production environments
    private String rlmIdProd1; // PROD1 environment RLM ID
    private String rlmIdProd2; // PROD2 environment RLM ID
    
    // Flag indicating if deployment is ready for production (final approval)
    private Boolean productionReady = false;

    // Getter and Setter methods with descriptive comments
    
    /**
     * @return The auto-generated ID of this deployment
     */
    public Long getId() { return id; }
    
    /**
     * @param id The ID to set (usually not called directly)
     */
    public void setId(Long id) { this.id = id; }
    
    /**
     * @return The unique serial number (MSDR format)
     */
    public String getSerialNumber() { return serialNumber; }
    
    /**
     * @param serialNumber The serial number to set (auto-generated)
     */
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    
    /**
     * @return The Customer Service Identifier
     */
    public String getCsiId() { return csiId; }
    
    /**
     * @param csiId The CSI ID to set
     */
    public void setCsiId(String csiId) { this.csiId = csiId; }
    
    /**
     * @return The service name being deployed
     */
    public String getService() { return service; }
    
    /**
     * @param service The service name to set
     */
    public void setService(String service) { this.service = service; }
    
    /**
     * @return The business request identifier
     */
    public String getRequestId() { return requestId; }
    
    /**
     * @param requestId The request ID to set
     */
    public void setRequestId(String requestId) { this.requestId = requestId; }
    
    /**
     * @return True if this is a configuration request
     */
    public Boolean getIsConfig() { return isConfig; }
    
    /**
     * @param isConfig Set to true for configuration requests
     */
    public void setIsConfig(Boolean isConfig) { this.isConfig = isConfig; }
    
    /**
     * @return The configuration request ID (only for config requests)
     */
    public String getConfigRequestId() { return configRequestId; }
    
    /**
     * @param configRequestId The config request ID to set
     */
    public void setConfigRequestId(String configRequestId) { this.configRequestId = configRequestId; }
    
    /**
     * @return List of target environments for this deployment
     */
    public List<String> getEnvironments() { return environments; }
    
    /**
     * @param environments List of environments to target
     */
    public void setEnvironments(List<String> environments) { this.environments = environments; }
    
    /**
     * @return The git release branch name
     */
    public String getReleaseBranch() { return releaseBranch; }
    
    /**
     * @param releaseBranch The release branch to set
     */
    public void setReleaseBranch(String releaseBranch) { this.releaseBranch = releaseBranch; }
    
    /**
     * @return The development team name
     */
    public String getTeam() { return team; }
    
    /**
     * @param team The team name to set
     */
    public void setTeam(String team) { this.team = team; }
    
    /**
     * @return When this request was created
     */
    public java.time.LocalDateTime getDateRequested() { return dateRequested; }
    
    /**
     * @param dateRequested The creation date to set
     */
    public void setDateRequested(java.time.LocalDateTime dateRequested) { this.dateRequested = dateRequested; }
    
    /**
     * @return When this request was last modified
     */
    public java.time.LocalDateTime getDateModified() { return dateModified; }
    
    /**
     * @param dateModified The modification date to set
     */
    public void setDateModified(java.time.LocalDateTime dateModified) { this.dateModified = dateModified; }
    
    /**
     * @return The release name (YYYY-MM format)
     */
    public String getRelease() { return release; }
    
    /**
     * @param release The release name to set
     */
    public void setRelease(String release) { this.release = release; }
    
    /**
     * @return Current deployment status
     */
    public String getStatus() { return status; }
    
    /**
     * @param status The status to set (Open, In Progress, Pending, Completed)
     */
    public void setStatus(String status) { this.status = status; }
    
    /**
     * @return Username of the person who created this request
     */
    public String getCreatedBy() { return createdBy; }
    
    /**
     * @param createdBy The creator username to set
     */
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    // RLM ID getters and setters for UAT environments
    public String getRlmIdUat1() { return rlmIdUat1; }
    public void setRlmIdUat1(String rlmIdUat1) { this.rlmIdUat1 = rlmIdUat1; }
    
    public String getRlmIdUat2() { return rlmIdUat2; }
    public void setRlmIdUat2(String rlmIdUat2) { this.rlmIdUat2 = rlmIdUat2; }
    
    public String getRlmIdUat3() { return rlmIdUat3; }
    public void setRlmIdUat3(String rlmIdUat3) { this.rlmIdUat3 = rlmIdUat3; }
    
    // RLM ID getters and setters for Performance environments
    public String getRlmIdPerf1() { return rlmIdPerf1; }
    public void setRlmIdPerf1(String rlmIdPerf1) { this.rlmIdPerf1 = rlmIdPerf1; }
    
    public String getRlmIdPerf2() { return rlmIdPerf2; }
    public void setRlmIdPerf2(String rlmIdPerf2) { this.rlmIdPerf2 = rlmIdPerf2; }
    
    // RLM ID getters and setters for Production environments
    public String getRlmIdProd1() { return rlmIdProd1; }
    public void setRlmIdProd1(String rlmIdProd1) { this.rlmIdProd1 = rlmIdProd1; }
    
    public String getRlmIdProd2() { return rlmIdProd2; }
    public void setRlmIdProd2(String rlmIdProd2) { this.rlmIdProd2 = rlmIdProd2; }
    
    /**
     * @return True if this deployment is marked as production ready
     */
    public Boolean getProductionReady() { return productionReady; }
    
    /**
     * @param productionReady Set to true when deployment is production ready
     * Note: Can only be set by original requester or superadmin, and only when status is "Completed"
     */
    public void setProductionReady(Boolean productionReady) { this.productionReady = productionReady; }
    
    /**
     * JPA lifecycle callback - called before persisting a new entity
     * Automatically sets the creation and modification timestamps
     */
    @PrePersist
    protected void onCreate() {
        // Set creation date if not already set
        if (dateRequested == null) {
            dateRequested = java.time.LocalDateTime.now();
        }
        // Always set modification date for new records
        dateModified = java.time.LocalDateTime.now();
    }
    
    /**
     * JPA lifecycle callback - called before updating an existing entity
     * Automatically updates the modification timestamp
     */
    @PreUpdate
    protected void onUpdate() {
        // Always update modification date when entity is updated
        dateModified = java.time.LocalDateTime.now();
    }
}