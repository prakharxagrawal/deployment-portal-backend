package com.deploymentportal.controller;

import com.deploymentportal.model.User;
import com.deploymentportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User user = userOpt.get();
            
            // Store user in session
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(12 * 60 * 60); // 12 hours
            
            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            response.put("sessionId", session.getId());
            
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/session")
    public ResponseEntity<?> getSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            response.put("sessionId", session.getId());
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(401).body(Map.of("error", "No active session"));
    }
}
