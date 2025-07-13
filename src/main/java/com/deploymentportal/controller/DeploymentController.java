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
@CrossOrigin(origins = "http://localhost:4200")
public class DeploymentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeploymentRepository deploymentRepository;

    @Autowired
    private ReleaseRepository releaseRepository;

    @PostMapping("/api/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return ResponseEntity.ok(Map.of(
                "username", user.get().getUsername(),
                "role", user.get().getRole()
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
        }
    }

    @PostMapping("/api/logout")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

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
    public List<Deployment> getDeployments() {
        return deploymentRepository.findAll();
    }

    @PostMapping("/api/deployments")
    public ResponseEntity<Map<String, String>> createDeployment(@RequestBody Deployment deployment) {
        deployment.setSerialNumber(generateSerialNumber());
        deployment.setStatus("Open");
        deploymentRepository.save(deployment);
        return ResponseEntity.ok(Map.of("message", "Deployment created"));
    }

    @PutMapping("/api/deployments/{id}")
    public ResponseEntity<Map<String, String>> updateDeployment(@PathVariable Long id, @RequestBody Deployment deployment) {
        Optional<Deployment> existing = deploymentRepository.findById(id);
        if (existing.isPresent()) {
            deployment.setId(id);
            deployment.setSerialNumber(existing.get().getSerialNumber());
            deployment.setStatus(existing.get().getStatus());
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
    public String generateReport(@RequestParam(required = false) String release, @RequestParam(required = false) String environment) {
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
        StringBuilder csv = new StringBuilder();
        csv.append("Serial Number,CSI ID,Services,Request IDs,Environments,Release Branch,Release,Status,Created By\n");
        for (Deployment d : deployments) {
            csv.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    d.getSerialNumber(),
                    d.getCsiId(),
                    String.join(";", d.getServices()),
                    String.join(";", d.getRequestIds()),
                    String.join(";", d.getEnvironments()),
                    d.getReleaseBranch(),
                    d.getRelease(),
                    d.getStatus(),
                    d.getCreatedBy()));
        }
        return csv.toString();
    }

    private String generateSerialNumber() {
        long count = deploymentRepository.count();
        return String.format("MSDR%07d", count + 1);
    }
}