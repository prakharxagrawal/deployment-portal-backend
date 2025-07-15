package com.deploymentportal.controller;

import com.deploymentportal.model.Deployment;
import com.deploymentportal.model.Release;
import com.deploymentportal.model.User;
import com.deploymentportal.repository.DeploymentRepository;
import com.deploymentportal.repository.ReleaseRepository;
import com.deploymentportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class DeploymentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeploymentRepository deploymentRepository;

    @Autowired
    private ReleaseRepository releaseRepository;

    @GetMapping("/api/releases")
    public List<Release> getReleases() {
        return releaseRepository.findAll();
    }

    @PostMapping("/api/releases")
    public ResponseEntity<Map<String, String>> createRelease(@RequestBody Map<String, String> release) {
        Release newRelease = new Release();
        newRelease.setName(release.get("release"));
        releaseRepository.save(newRelease);
        return ResponseEntity.ok(Map.of("message", "Release created"));
    }

    @GetMapping("/api/deployments")
    public List<Deployment> getDeployments(@RequestParam(required = false) String search, 
                                          @RequestParam(required = false) String config) {
        List<Deployment> deployments = deploymentRepository.findAll();
        
        // Sort by latest dateRequested (descending) - latest requests at the top
        deployments.sort((a, b) -> {
            if (a.getDateRequested() == null && b.getDateRequested() == null) return 0;
            if (a.getDateRequested() == null) return 1;
            if (b.getDateRequested() == null) return -1;
            return b.getDateRequested().compareTo(a.getDateRequested());
        });
        
        // Apply config filter first
        if (config != null && !config.trim().isEmpty()) {
            if ("yes".equalsIgnoreCase(config.trim())) {
                deployments = deployments.stream()
                    .filter(d -> d.getIsConfig() != null && d.getIsConfig())
                    .collect(Collectors.toList());
            } else if ("no".equalsIgnoreCase(config.trim())) {
                deployments = deployments.stream()
                    .filter(d -> d.getIsConfig() == null || !d.getIsConfig())
                    .collect(Collectors.toList());
            }
        }
        
        // Apply universal search filter - matches on MSDR number, date, and service name
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase().trim();
            deployments = deployments.stream()
                .filter(d -> 
                    // Search MSDR number (serial number)
                    (d.getSerialNumber() != null && d.getSerialNumber().toLowerCase().contains(searchLower)) ||
                    // Search service name
                    (d.getService() != null && d.getService().toLowerCase().contains(searchLower)) ||
                    // Search dates (both requested and modified)
                    (d.getDateRequested() != null && d.getDateRequested().toString().contains(searchLower)) ||
                    (d.getDateModified() != null && d.getDateModified().toString().contains(searchLower)) ||
                    // Additional useful fields for search
                    (d.getCsiId() != null && d.getCsiId().toLowerCase().contains(searchLower)) ||
                    (d.getRequestId() != null && d.getRequestId().toLowerCase().contains(searchLower)) ||
                    (d.getTeam() != null && d.getTeam().toLowerCase().contains(searchLower)) ||
                    (d.getRelease() != null && d.getRelease().toLowerCase().contains(searchLower)) ||
                    (d.getStatus() != null && d.getStatus().toLowerCase().contains(searchLower))
                )
                .collect(Collectors.toList());
        }
        
        return deployments;
    }

    // Keep the original getDeployments method for backward compatibility
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

    @PostMapping("/api/deployments")
    public ResponseEntity<Map<String, String>> createDeployment(@RequestBody Deployment deployment) {
        deployment.setSerialNumber(generateSerialNumber());
        deployment.setStatus("Open");
        // Set correct dateRequested - preserve if provided, otherwise set current time
        if (deployment.getDateRequested() == null) {
            deployment.setDateRequested(java.time.LocalDateTime.now());
        }
        deployment.setDateModified(java.time.LocalDateTime.now());
        deploymentRepository.save(deployment);
        return ResponseEntity.ok(Map.of("message", "Deployment created"));
    }

    @PutMapping("/api/deployments/{id}")
    public ResponseEntity<Map<String, String>> updateDeployment(@PathVariable Long id, @RequestBody Deployment deployment) {
        Optional<Deployment> existing = deploymentRepository.findById(id);
        if (existing.isPresent()) {
            Deployment existingDeployment = existing.get();
            
            // Preserve core fields that shouldn't change
            deployment.setId(id);
            deployment.setSerialNumber(existingDeployment.getSerialNumber());
            deployment.setDateRequested(existingDeployment.getDateRequested());
            deployment.setDateModified(java.time.LocalDateTime.now());
            
            // Allow status updates (admin feature)
            if (deployment.getStatus() != null && !deployment.getStatus().trim().isEmpty()) {
                // Validate status values
                String status = deployment.getStatus().trim();
                if (status.equals("Open") || status.equals("In Progress") || 
                    status.equals("Pending") || status.equals("Completed")) {
                    deployment.setStatus(status);
                } else {
                    deployment.setStatus(existingDeployment.getStatus()); // Keep existing if invalid
                }
            } else {
                deployment.setStatus(existingDeployment.getStatus()); // Keep existing if null/empty
            }
            
            deploymentRepository.save(deployment);
            return ResponseEntity.ok(Map.of("message", "Deployment updated"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Deployment not found"));
        }
    }

    @DeleteMapping("/api/deployments/{id}")
    public ResponseEntity<Map<String, String>> deleteDeployment(@PathVariable Long id) {
        if (deploymentRepository.existsById(id)) {
            deploymentRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Deployment deleted"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Deployment not found"));
        }
    }

    @GetMapping("/api/reports/general")
    public ResponseEntity<byte[]> generateReport(@RequestParam(required = false) String release,
                                                @RequestParam(required = false) String environment,
                                                @RequestParam(required = false) String team) {
        List<Deployment> deployments = deploymentRepository.findAll();
        if (release != null && !release.isEmpty()) {
            deployments = deployments.stream()
                    .filter(d -> release.equals(d.getRelease()))
                    .collect(Collectors.toList());
        }
        if (environment != null && !environment.isEmpty()) {
            deployments = deployments.stream()
                    .filter(d -> d.getEnvironments().contains(environment))
                    .collect(Collectors.toList());
        }
        if (team != null && !team.isEmpty()) {
            deployments = deployments.stream()
                    .filter(d -> team.equalsIgnoreCase(d.getTeam()))
                    .collect(Collectors.toList());
        }
        StringBuilder csv = new StringBuilder();
        csv.append("Serial Number,CSI ID,Service,Request ID,Environments,Team,Release,Status,Created By,Date Requested,Date Modified,RLM ID DEV1,RLM ID DEV2,RLM ID DEV3,RLM ID UAT1,RLM ID UAT2,RLM ID UAT3,RLM ID PERF1,RLM ID PERF2,RLM ID PROD1,RLM ID PROD2\n");
        for (Deployment d : deployments) {
            csv.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    d.getSerialNumber(),
                    d.getCsiId(),
                    d.getService() != null ? d.getService() : "",
                    d.getRequestId() != null ? d.getRequestId() : "",
                    String.join(";", d.getEnvironments()),
                    d.getTeam() != null ? d.getTeam() : "",
                    d.getRelease(),
                    d.getStatus(),
                    d.getCreatedBy(),
                    d.getDateRequested() != null ? d.getDateRequested().toString() : "",
                    d.getDateModified() != null ? d.getDateModified().toString() : "",
                    d.getRlmIdDev1() != null ? d.getRlmIdDev1() : "",
                    d.getRlmIdDev2() != null ? d.getRlmIdDev2() : "",
                    d.getRlmIdDev3() != null ? d.getRlmIdDev3() : "",
                    d.getRlmIdUat1() != null ? d.getRlmIdUat1() : "",
                    d.getRlmIdUat2() != null ? d.getRlmIdUat2() : "",
                    d.getRlmIdUat3() != null ? d.getRlmIdUat3() : "",
                    d.getRlmIdPerf1() != null ? d.getRlmIdPerf1() : "",
                    d.getRlmIdPerf2() != null ? d.getRlmIdPerf2() : "",
                    d.getRlmIdProd1() != null ? d.getRlmIdProd1() : "",
                    d.getRlmIdProd2() != null ? d.getRlmIdProd2() : ""));
        }
        
        byte[] csvData = csv.toString().getBytes();
        String filename = String.format("deployments_%s_%s_%s.csv", 
            release != null ? release : "all",
            environment != null ? environment : "all", 
            team != null ? team : "all");
            
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .header("Content-Type", "text/csv")
                .body(csvData);
    }

    private String generateSerialNumber() {
        long count = deploymentRepository.count();
        return String.format("MSDR%07d", count + 1);
    }
}