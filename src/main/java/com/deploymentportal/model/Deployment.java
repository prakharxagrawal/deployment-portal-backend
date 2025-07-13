package com.deploymentportal.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class Deployment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serialNumber;
    private String csiId;
    @ElementCollection
    @CollectionTable(name = "deployment_services", joinColumns = @JoinColumn(name = "deployment_id"))
    @Column(name = "services")
    private List<String> services;

    @ElementCollection
    @CollectionTable(name = "deployment_request_ids", joinColumns = @JoinColumn(name = "deployment_id"))
    @Column(name = "request_ids")
    private List<String> requestIds;

    @ElementCollection
    @CollectionTable(name = "deployment_environments", joinColumns = @JoinColumn(name = "deployment_id"))
    @Column(name = "environments")
    private List<String> environments;
    private String releaseBranch;
    private String release;
    private String status;
    private String createdBy;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public String getCsiId() { return csiId; }
    public void setCsiId(String csiId) { this.csiId = csiId; }
    public List<String> getServices() { return services; }
    public void setServices(List<String> services) { this.services = services; }
    public List<String> getRequestIds() { return requestIds; }
    public void setRequestIds(List<String> requestIds) { this.requestIds = requestIds; }
    public List<String> getEnvironments() { return environments; }
    public void setEnvironments(List<String> environments) { this.environments = environments; }
    public String getReleaseBranch() { return releaseBranch; }
    public void setReleaseBranch(String releaseBranch) { this.releaseBranch = releaseBranch; }
    public String getRelease() { return release; }
    public void setRelease(String release) { this.release = release; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}