package com.deploymentportal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String username;
    private String password;
    private String role;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() {
        if ("user".equalsIgnoreCase(role)) return "developer";
        return role;
    }
    public void setRole(String role) {
        if ("user".equalsIgnoreCase(role)) this.role = "developer";
        else this.role = role;
    }
}