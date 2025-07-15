package com.deploymentportal.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.List;

@Entity
public class Deployment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serialNumber;
    private String csiId;
    
    // Single service instead of list
    private String service;
    
    // Single request ID instead of list
    private String requestId;
    
    // Config support
    private Boolean isConfig = false;
    private String configRequestId;

    @ElementCollection
    @CollectionTable(name = "deployment_environments", joinColumns = @JoinColumn(name = "deployment_id"))
    @Column(name = "environments")
    private List<String> environments;
    private String releaseBranch;
    private String team;
    private java.time.LocalDateTime dateRequested;
    private java.time.LocalDateTime dateModified;
    private String release;
    private String status = "Open"; // Default status
    private String createdBy;
    
    // RLM IDs for each environment
    private String rlmIdUat1;
    private String rlmIdUat2;
    private String rlmIdUat3;
    private String rlmIdDev1;
    private String rlmIdDev2;
    private String rlmIdDev3;
    private String rlmIdPerf1;
    private String rlmIdPerf2;
    private String rlmIdProd1;
    private String rlmIdProd2;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public String getCsiId() { return csiId; }
    public void setCsiId(String csiId) { this.csiId = csiId; }
    
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    
    // Config getters and setters
    public Boolean getIsConfig() { return isConfig; }
    public void setIsConfig(Boolean isConfig) { this.isConfig = isConfig; }
    public String getConfigRequestId() { return configRequestId; }
    public void setConfigRequestId(String configRequestId) { this.configRequestId = configRequestId; }
    
    public List<String> getEnvironments() { return environments; }
    public void setEnvironments(List<String> environments) { this.environments = environments; }
    public String getReleaseBranch() { return releaseBranch; }
    public void setReleaseBranch(String releaseBranch) { this.releaseBranch = releaseBranch; }
    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }
    public java.time.LocalDateTime getDateRequested() { return dateRequested; }
    public void setDateRequested(java.time.LocalDateTime dateRequested) { this.dateRequested = dateRequested; }
    public java.time.LocalDateTime getDateModified() { return dateModified; }
    public void setDateModified(java.time.LocalDateTime dateModified) { this.dateModified = dateModified; }
    public String getRelease() { return release; }
    public void setRelease(String release) { this.release = release; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    // RLM ID getters and setters
    public String getRlmIdUat1() { return rlmIdUat1; }
    public void setRlmIdUat1(String rlmIdUat1) { this.rlmIdUat1 = rlmIdUat1; }
    public String getRlmIdUat2() { return rlmIdUat2; }
    public void setRlmIdUat2(String rlmIdUat2) { this.rlmIdUat2 = rlmIdUat2; }
    public String getRlmIdUat3() { return rlmIdUat3; }
    public void setRlmIdUat3(String rlmIdUat3) { this.rlmIdUat3 = rlmIdUat3; }
    public String getRlmIdDev1() { return rlmIdDev1; }
    public void setRlmIdDev1(String rlmIdDev1) { this.rlmIdDev1 = rlmIdDev1; }
    public String getRlmIdDev2() { return rlmIdDev2; }
    public void setRlmIdDev2(String rlmIdDev2) { this.rlmIdDev2 = rlmIdDev2; }
    public String getRlmIdDev3() { return rlmIdDev3; }
    public void setRlmIdDev3(String rlmIdDev3) { this.rlmIdDev3 = rlmIdDev3; }
    public String getRlmIdPerf1() { return rlmIdPerf1; }
    public void setRlmIdPerf1(String rlmIdPerf1) { this.rlmIdPerf1 = rlmIdPerf1; }
    public String getRlmIdPerf2() { return rlmIdPerf2; }
    public void setRlmIdPerf2(String rlmIdPerf2) { this.rlmIdPerf2 = rlmIdPerf2; }
    public String getRlmIdProd1() { return rlmIdProd1; }
    public void setRlmIdProd1(String rlmIdProd1) { this.rlmIdProd1 = rlmIdProd1; }
    public String getRlmIdProd2() { return rlmIdProd2; }
    public void setRlmIdProd2(String rlmIdProd2) { this.rlmIdProd2 = rlmIdProd2; }
    
    @PrePersist
    protected void onCreate() {
        if (dateRequested == null) {
            dateRequested = java.time.LocalDateTime.now();
        }
        dateModified = java.time.LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        dateModified = java.time.LocalDateTime.now();
    }
}