package com.deploymentportal.controller;

// Import necessary model classes for data structures
import com.deploymentportal.model.Deployment;
import com.deploymentportal.model.Release;
import com.deploymentportal.model.Service;
import com.deploymentportal.model.User;

// Import repository interfaces for database operations
import com.deploymentportal.repository.DeploymentRepository;
import com.deploymentportal.repository.ReleaseRepository;
import com.deploymentportal.repository.ServiceRepository;

// Spring Framework imports for REST API functionality
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Servlet imports for session management
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// Java utility imports for data manipulation
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Main REST Controller for the Deployment Portal Application
 * 
 * This controller handles all HTTP requests related to:
 * - Deployment management (CRUD operations)
 * - Release management (create and list releases)
 * - Service management (search and list services)
 * - Report generation (CSV export with filtering)
 * 
 * The controller uses Spring Boot annotations to define REST endpoints
 * and includes session-based authentication for certain operations.
 * 
 * @author Deployment Portal Team
 * @version 1.0
 */
@RestController // Indicates this class handles REST requests and returns JSON responses
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true") // Allows requests from Angular frontend with credentials
public class DeploymentController {

    // ===== DEPENDENCY INJECTION =====
    // Spring automatically injects these repository beans to interact with the database
    
    /**
     * Repository for deployment-related database operations
     * Provides CRUD operations for Deployment entities
     */
    @Autowired
    private DeploymentRepository deploymentRepository;

    /**
     * Repository for release-related database operations
     * Handles release creation, retrieval, and validation
     */
    @Autowired
    private ReleaseRepository releaseRepository;

    /**
     * Repository for service-related database operations
     * Manages service lookup and search functionality
     */
    @Autowired
    private ServiceRepository serviceRepository;

    // ===== RELEASE MANAGEMENT ENDPOINTS =====
    
    /**
     * GET /api/releases - Retrieve all releases
     * 
     * Returns a list of all releases in the system, ordered by name (descending)
     * so that the latest releases (e.g., 2024-12) appear first.
     * 
     * @return List<Release> - All releases ordered by name descending
     */
    @GetMapping("/api/releases")
    public List<Release> getReleases() {
        // Call repository method to get releases sorted by name (latest first)
        return releaseRepository.findAllOrderByNameDesc();
    }

    /**
     * POST /api/releases - Create a new release
     * 
     * This endpoint allows administrators to create new releases.
     * Performs extensive validation:
     * - Checks for required fields (name and description)
     * - Validates YYYY-MM format for release names
     * - Prevents duplicate release names
     * 
     * @param releaseData Release object from request body containing name and description
     * @return ResponseEntity with success/error message and created release data
     */
    @PostMapping("/api/releases")
    public ResponseEntity<?> createRelease(@RequestBody Release releaseData) {
        try {
            // VALIDATION STEP 1: Check if release name is provided
            if (releaseData.getName() == null || releaseData.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Release name is required"));
            }
            
            // VALIDATION STEP 2: Check if description is provided
            if (releaseData.getDescription() == null || releaseData.getDescription().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Description is required"));
            }
            
            // VALIDATION STEP 3: Validate YYYY-MM format using regex
            // Pattern: ^ = start, \d{4} = exactly 4 digits, - = literal dash, \d{2} = exactly 2 digits, $ = end
            if (!releaseData.getName().matches("^\\d{4}-\\d{2}$")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Release name must be in YYYY-MM format"));
            }
            
            // VALIDATION STEP 4: Check for existing release with same name
            Release existingRelease = releaseRepository.findByName(releaseData.getName().trim());
            if (existingRelease != null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Release with this name already exists"));
            }
            
            // CREATE NEW RELEASE: All validations passed, create and save the release
            Release newRelease = new Release();
            newRelease.setName(releaseData.getName().trim()); // Remove any whitespace
            newRelease.setDescription(releaseData.getDescription().trim());
            
            // Save to database
            releaseRepository.save(newRelease);
            
            // Return success response with the created release
            return ResponseEntity.ok(Map.of("message", "Release created successfully", "release", newRelease));
        } catch (Exception e) {
            // Handle any unexpected errors during release creation
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to create release: " + e.getMessage()));
        }
    }

    // ===== DEPLOYMENT MANAGEMENT ENDPOINTS =====
    
    /**
     * GET /api/deployments - Retrieve deployments with optional filtering
     * 
     * This is the main endpoint for fetching deployments with advanced filtering capabilities:
     * - Universal search across multiple fields (MSDR, service, dates, team, etc.)
     * - Configuration filter (yes/no for config deployments)
     * - Results are automatically sorted by date requested (latest first)
     * 
     * @param search Optional search term to filter deployments across multiple fields
     * @param config Optional filter for configuration deployments ("yes"/"no")
     * @return List<Deployment> - Filtered and sorted list of deployments
     */
    @GetMapping("/api/deployments")
    public List<Deployment> getDeployments(@RequestParam(required = false) String search, 
                                          @RequestParam(required = false) String config) {
        // STEP 1: Get all deployments from database
        List<Deployment> deployments = deploymentRepository.findAll();
        
        // STEP 2: Sort deployments by latest date requested (descending order)
        // This ensures newest deployment requests appear at the top of the list
        deployments.sort((a, b) -> {
            // Handle null dates - put null dates at the bottom
            if (a.getDateRequested() == null && b.getDateRequested() == null) return 0;
            if (a.getDateRequested() == null) return 1; // a goes to bottom
            if (b.getDateRequested() == null) return -1; // b goes to bottom
            // Compare dates - b.compareTo(a) gives descending order (latest first)
            return b.getDateRequested().compareTo(a.getDateRequested());
        });
        
        // STEP 3: Apply configuration filter if provided
        if (config != null && !config.trim().isEmpty()) {
            if ("yes".equalsIgnoreCase(config.trim())) {
                // Filter to show only configuration deployments
                deployments = deployments.stream()
                    .filter(d -> d.getIsConfig() != null && d.getIsConfig())
                    .collect(Collectors.toList());
            } else if ("no".equalsIgnoreCase(config.trim())) {
                // Filter to show only non-configuration deployments
                deployments = deployments.stream()
                    .filter(d -> d.getIsConfig() == null || !d.getIsConfig())
                    .collect(Collectors.toList());
            }
        }
        
        // STEP 4: Apply universal search filter if search term is provided
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase().trim();
            deployments = deployments.stream()
                .filter(d -> 
                    // Search across MSDR number (serial number)
                    (d.getSerialNumber() != null && d.getSerialNumber().toLowerCase().contains(searchLower)) ||
                    // Search across service name
                    (d.getService() != null && d.getService().toLowerCase().contains(searchLower)) ||
                    // Search across dates (both requested and modified dates)
                    (d.getDateRequested() != null && d.getDateRequested().toString().contains(searchLower)) ||
                    (d.getDateModified() != null && d.getDateModified().toString().contains(searchLower)) ||
                    // Additional searchable fields for comprehensive search
                    (d.getCsiId() != null && d.getCsiId().toLowerCase().contains(searchLower)) ||
                    (d.getRequestId() != null && d.getRequestId().toLowerCase().contains(searchLower)) ||
                    (d.getTeam() != null && d.getTeam().toLowerCase().contains(searchLower)) ||
                    (d.getRelease() != null && d.getRelease().toLowerCase().contains(searchLower)) ||
                    (d.getStatus() != null && d.getStatus().toLowerCase().contains(searchLower))
                )
                .collect(Collectors.toList());
        }
        
        // Return the filtered and sorted list
        return deployments;
    }

    /**
     * GET /api/deployments/all - Legacy endpoint for backward compatibility
     * 
     * This endpoint maintains backward compatibility with older frontend versions.
     * It returns all deployments without any filtering, sorted by date requested.
     * 
     * @return List<Deployment> - All deployments sorted by date requested (latest first)
     */
    @GetMapping("/api/deployments/all")
    public List<Deployment> getAllDeployments() {
        List<Deployment> deployments = deploymentRepository.findAll();
        // Sort by latest dateRequested (descending) - latest requests at the top
        deployments.sort((a, b) -> {
            if (a.getDateRequested() == null && b.getDateRequested() == null) return 0;
            if (a.getDateRequested() == null) return 1;
            if (b.getDateRequested() == null) return -1;
            return b.getDateRequested().compareTo(a.getDateRequested());
        });
        return deployments;
    }

    /**
     * POST /api/deployments - Create a new deployment request
     * 
     * This endpoint creates a new deployment request with auto-generated values:
     * - Generates unique MSDR serial number
     * - Sets initial status to "Open"
     * - Sets creation and modification timestamps
     * 
     * @param deployment Deployment object from request body
     * @return ResponseEntity with success message
     */
    @PostMapping("/api/deployments")
    public ResponseEntity<Map<String, String>> createDeployment(@RequestBody Deployment deployment) {
        // Generate unique MSDR serial number (e.g., MSDR0000001)
        deployment.setSerialNumber(generateSerialNumber());
        
        // Set initial status for new deployment requests
        deployment.setStatus("Open");
        
        // Set creation timestamp - preserve if already provided, otherwise use current time
        if (deployment.getDateRequested() == null) {
            deployment.setDateRequested(java.time.LocalDateTime.now());
        }
        
        // Always set the modification timestamp to current time
        deployment.setDateModified(java.time.LocalDateTime.now());
        
        // Save the new deployment to database
        deploymentRepository.save(deployment);
        
        // Return success response
        return ResponseEntity.ok(Map.of("message", "Deployment created"));
    }

    /**
     * PUT /api/deployments/{id} - Update an existing deployment
     * 
     * This endpoint handles deployment updates with security and business rule checks:
     * - Validates user session for authentication
     * - Enforces production ready permissions (only requester or superadmin)
     * - Validates status transitions and values
     * - Preserves core immutable fields (serial number, creation date)
     * 
     * @param id Deployment ID to update
     * @param deployment Updated deployment data
     * @param request HTTP request to access session information
     * @return ResponseEntity with success/error message
     */
    @PutMapping("/api/deployments/{id}")
    public ResponseEntity<Map<String, String>> updateDeployment(@PathVariable Long id, @RequestBody Deployment deployment, HttpServletRequest request) {
        // SECURITY CHECK: Validate user session
        HttpSession session = request.getSession(false); // false = don't create new session
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }

        // Get current logged-in user from session
        User currentUser = (User) session.getAttribute("user");
        
        // FIND EXISTING DEPLOYMENT: Check if deployment exists in database
        Optional<Deployment> existing = deploymentRepository.findById(id);
        if (existing.isPresent()) {
            Deployment existingDeployment = existing.get();
            
            // PRODUCTION READY PERMISSION CHECK: Special handling for production ready flag
            if (deployment.getProductionReady() != null && deployment.getProductionReady() != existingDeployment.getProductionReady()) {
                // Business Rule 1: Can only mark as production ready when status is "Completed"
                if (!"Completed".equals(existingDeployment.getStatus())) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Can only mark as production ready when status is Completed"));
                }
                
                // Business Rule 2: Only requester (creator) or superadmin can mark as production ready
                if (!(currentUser.getRole().equals("superadmin") || 
                      currentUser.getUsername().equals(existingDeployment.getCreatedBy()))) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Only requester or super admin can mark as production ready"));
                }
            }
            
            // PRESERVE CORE FIELDS: Set immutable fields that should never change
            deployment.setId(id); // Ensure ID matches the URL parameter
            deployment.setSerialNumber(existingDeployment.getSerialNumber()); // MSDR number never changes
            deployment.setDateRequested(existingDeployment.getDateRequested()); // Creation date never changes
            deployment.setDateModified(java.time.LocalDateTime.now()); // Always update modification time
            
            // STATUS VALIDATION: Validate status updates if provided
            if (deployment.getStatus() != null && !deployment.getStatus().trim().isEmpty()) {
                // Clean up the status value
                String status = deployment.getStatus().trim();
                
                // Validate against allowed status values
                if (status.equals("Open") || status.equals("In Progress") || 
                    status.equals("Pending") || status.equals("Completed")) {
                    deployment.setStatus(status); // Valid status - use it
                } else {
                    deployment.setStatus(existingDeployment.getStatus()); // Invalid status - keep existing
                }
            } else {
                // No status provided - keep existing status
                deployment.setStatus(existingDeployment.getStatus());
            }
            
            // PRODUCTION READY FIELD: Handle production ready flag
            if (deployment.getProductionReady() == null) {
                // If not provided in update, preserve existing value
                deployment.setProductionReady(existingDeployment.getProductionReady());
            }
            
            // SAVE UPDATED DEPLOYMENT: Persist changes to database
            deploymentRepository.save(deployment);
            return ResponseEntity.ok(Map.of("message", "Deployment updated"));
        } else {
            // Deployment with given ID not found
            return ResponseEntity.badRequest().body(Map.of("error", "Deployment not found"));
        }
    }

    /**
     * DELETE /api/deployments/{id} - Delete a deployment request
     * 
     * This endpoint allows complete removal of a deployment from the system.
     * Used primarily for cleanup of test data or incorrect entries.
     * 
     * @param id Deployment ID to delete
     * @return ResponseEntity with success/error message
     */
    @DeleteMapping("/api/deployments/{id}")
    public ResponseEntity<Map<String, String>> deleteDeployment(@PathVariable Long id) {
        // Check if deployment exists before attempting deletion
        if (deploymentRepository.existsById(id)) {
            // Delete the deployment from database
            deploymentRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Deployment deleted"));
        } else {
            // Deployment not found
            return ResponseEntity.badRequest().body(Map.of("error", "Deployment not found"));
        }
    }

    // ===== REPORTING AND EXPORT ENDPOINTS =====
    
    /**
     * GET /api/reports/general - Generate and export deployment report as CSV
     * 
     * This endpoint generates comprehensive CSV reports with advanced filtering options:
     * - Date range filtering (start and end dates)
     * - Release filtering (specific release versions)
     * - Environment filtering (UAT1, UAT2, UAT3, PERF, PROD)
     * - Team filtering (by team name)
     * 
     * The generated CSV includes all deployment details and RLM IDs for audit purposes.
     * 
     * @param release Optional filter by release name
     * @param environment Optional filter by environment type
     * @param team Optional filter by team name
     * @param startDate Optional start date for date range filter (YYYY-MM-DD format)
     * @param endDate Optional end date for date range filter (YYYY-MM-DD format)
     * @return ResponseEntity<byte[]> CSV file as downloadable response
     */
    @GetMapping("/api/reports/general")
    public ResponseEntity<byte[]> generateReport(@RequestParam(required = false) String release,
                                                @RequestParam(required = false) String environment,
                                                @RequestParam(required = false) String team,
                                                @RequestParam(required = false) String startDate,
                                                @RequestParam(required = false) String endDate) {
        // STEP 1: Get all deployments from database
        List<Deployment> deployments = deploymentRepository.findAll();
        
        // STEP 2: Apply date range filter if both start and end dates are provided
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            try {
                // Parse dates and create full day ranges
                // Add T00:00:00 to start date to include from beginning of day
                java.time.LocalDateTime startDateTime = java.time.LocalDateTime.parse(startDate + "T00:00:00");
                // Add T23:59:59 to end date to include until end of day
                java.time.LocalDateTime endDateTime = java.time.LocalDateTime.parse(endDate + "T23:59:59");
                
                // Filter deployments within the date range
                deployments = deployments.stream()
                    .filter(d -> d.getDateRequested() != null && 
                               !d.getDateRequested().isBefore(startDateTime) && 
                               !d.getDateRequested().isAfter(endDateTime))
                    .collect(Collectors.toList());
            } catch (Exception e) {
                // If date parsing fails, ignore the date filter and continue
                // This prevents the entire report from failing due to malformed dates
            }
        }
        
        // STEP 3: Apply release filter if specified
        if (release != null && !release.isEmpty()) {
            deployments = deployments.stream()
                    .filter(d -> release.equals(d.getRelease()))
                    .collect(Collectors.toList());
        }
        
        // STEP 4: Apply environment filter if specified
        if (environment != null && !environment.isEmpty()) {
            deployments = deployments.stream()
                    .filter(d -> {
                        // Handle consolidated environment filtering based on RLM ID presence
                        switch (environment.toUpperCase()) {
                            case "UAT1":
                                return d.getRlmIdUat1() != null;
                            case "UAT2":
                                return d.getRlmIdUat2() != null;
                            case "UAT3":
                                return d.getRlmIdUat3() != null;
                            case "PERF":
                                // PERF includes both PERF1 and PERF2
                                return d.getRlmIdPerf1() != null || d.getRlmIdPerf2() != null;
                            case "PROD":
                                // PROD includes both PROD1 and PROD2
                                return d.getRlmIdProd1() != null || d.getRlmIdProd2() != null;
                            default:
                                return false; // Unknown environment
                        }
                    })
                    .collect(Collectors.toList());
        }
        
        // STEP 5: Apply team filter if specified
        if (team != null && !team.isEmpty()) {
            deployments = deployments.stream()
                    .filter(d -> team.equalsIgnoreCase(d.getTeam()))
                    .collect(Collectors.toList());
        }
        
        // STEP 6: Generate CSV content
        StringBuilder csv = new StringBuilder();
        
        // Add CSV header row with all column names
        csv.append("Serial Number,CSI ID,Service,Request ID,Environments,Team,Release,Status,Created By,Date Requested,Date Modified,RLM ID UAT1,RLM ID UAT2,RLM ID UAT3,RLM ID PERF1,RLM ID PERF2,RLM ID PROD1,RLM ID PROD2\n");
        
        // STEP 7: Add data rows for each deployment
        for (Deployment d : deployments) {
            // Create a consolidated environments string based on RLM ID presence
            StringBuilder environments = new StringBuilder();
            if (d.getRlmIdUat1() != null) environments.append("UAT1;");
            if (d.getRlmIdUat2() != null) environments.append("UAT2;");
            if (d.getRlmIdUat3() != null) environments.append("UAT3;");
            if (d.getRlmIdPerf1() != null || d.getRlmIdPerf2() != null) environments.append("PERF;");
            if (d.getRlmIdProd1() != null || d.getRlmIdProd2() != null) environments.append("PROD;");
            
            // Add CSV row with all deployment data (handle null values)
            csv.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    d.getSerialNumber(),
                    d.getCsiId(),
                    d.getService() != null ? d.getService() : "",
                    d.getRequestId() != null ? d.getRequestId() : "",
                    environments.toString(),
                    d.getTeam() != null ? d.getTeam() : "",
                    d.getRelease(),
                    d.getStatus(),
                    d.getCreatedBy(),
                    d.getDateRequested() != null ? d.getDateRequested().toString() : "",
                    d.getDateModified() != null ? d.getDateModified().toString() : "",
                    d.getRlmIdUat1() != null ? d.getRlmIdUat1() : "",
                    d.getRlmIdUat2() != null ? d.getRlmIdUat2() : "",
                    d.getRlmIdUat3() != null ? d.getRlmIdUat3() : "",
                    d.getRlmIdPerf1() != null ? d.getRlmIdPerf1() : "",
                    d.getRlmIdPerf2() != null ? d.getRlmIdPerf2() : "",
                    d.getRlmIdProd1() != null ? d.getRlmIdProd1() : "",
                    d.getRlmIdProd2() != null ? d.getRlmIdProd2() : ""));
        }
        
        // STEP 8: Convert CSV string to bytes for download
        byte[] csvData = csv.toString().getBytes();
        
        // STEP 9: Generate descriptive filename based on applied filters
        String filename = String.format("deployments_%s_%s_%s.csv", 
            release != null ? release : "all",
            environment != null ? environment : "all", 
            team != null ? team : "all");
            
        // STEP 10: Return CSV file as downloadable response
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .header("Content-Type", "text/csv")
                .body(csvData);
    }

    // ===== UTILITY METHODS =====
    
    /**
     * Generate unique MSDR serial number for new deployments
     * 
     * Creates sequential MSDR numbers in format: MSDR0000001, MSDR0000002, etc.
     * Uses the current count of deployments + 1 to ensure uniqueness.
     * 
     * @return String - Formatted MSDR serial number
     */
    private String generateSerialNumber() {
        // Get current count of deployments in database
        long count = deploymentRepository.count();
        
        // Generate next MSDR number with 7-digit padding (e.g., MSDR0000001)
        return String.format("MSDR%07d", count + 1);
    }

    // ===== SERVICE MANAGEMENT ENDPOINTS =====
    
    /**
     * GET /api/services - Retrieve all services
     * 
     * Returns a complete list of services available in the system,
     * sorted alphabetically by name for easy selection in forms.
     * 
     * @return ResponseEntity<List<Service>> - All services sorted by name
     */
    @GetMapping("/api/services")
    public ResponseEntity<List<Service>> getAllServices() {
        // Get all services sorted alphabetically by name
        List<Service> services = serviceRepository.findAllByOrderByNameAsc();
        return ResponseEntity.ok(services);
    }

    /**
     * GET /api/services/search - Search services by name
     * 
     * Provides typeahead/autocomplete functionality for service selection.
     * Performs case-insensitive partial matching on service names.
     * 
     * @param query Search term to match against service names
     * @return ResponseEntity<List<Service>> - Services matching the search query
     */
    @GetMapping("/api/services/search")
    public ResponseEntity<List<Service>> searchServices(@RequestParam String query) {
        // Perform case-insensitive search for services containing the query string
        List<Service> services = serviceRepository.findByNameContainingIgnoreCase(query);
        return ResponseEntity.ok(services);
    }
}