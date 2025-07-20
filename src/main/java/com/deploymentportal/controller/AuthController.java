package com.deploymentportal.controller;

// Import necessary model and repository classes
import com.deploymentportal.model.User;
import com.deploymentportal.repository.UserRepository;

// Spring Framework imports for REST API functionality
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Session management imports
import jakarta.servlet.http.HttpSession;

// Java utility imports for data structures
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Authentication Controller for the Deployment Portal
 * 
 * This controller handles all authentication-related operations:
 * - User login with session creation
 * - User logout with session invalidation
 * - Session validation and user info retrieval
 * 
 * Uses HTTP sessions for state management with 12-hour timeout.
 * All endpoints support CORS for frontend integration.
 * 
 * @author Deployment Portal Team
 * @version 1.0
 */
@RestController // Indicates this class handles REST requests and returns JSON responses
@RequestMapping("/api") // All endpoints in this controller are prefixed with /api
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true") // Allow Angular frontend with credentials
public class AuthController {

    /**
     * Repository for user-related database operations
     * Handles user authentication and retrieval
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * POST /api/login - Authenticate user and create session
     * 
     * This endpoint handles user authentication by:
     * 1. Validating username and password against database
     * 2. Creating HTTP session for authenticated users
     * 3. Setting 12-hour session timeout
     * 4. Returning user information and session ID
     * 
     * @param credentials Map containing username and password
     * @param session HTTP session object managed by Spring
     * @return ResponseEntity with user info and session details, or error message
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        // Extract credentials from request body
        String username = credentials.get("username");
        String password = credentials.get("password");

        // STEP 1: Look up user by username in database
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        // STEP 2: Validate user exists and password matches
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User user = userOpt.get();
            
            // STEP 3: Create user session
            session.setAttribute("user", user); // Store user object in session
            session.setMaxInactiveInterval(12 * 60 * 60); // Set 12-hour timeout (12 * 60 minutes * 60 seconds)
            
            // STEP 4: Prepare success response with user details
            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("role", user.getRole()); // Important for frontend role-based features
            response.put("sessionId", session.getId()); // For debugging/tracking purposes
            
            return ResponseEntity.ok(response);
        }
        
        // STEP 5: Return error for invalid credentials
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }

    /**
     * POST /api/logout - End user session
     * 
     * This endpoint handles user logout by:
     * 1. Invalidating the current HTTP session
     * 2. Clearing all session data
     * 3. Returning success confirmation
     * 
     * @param session Current HTTP session to invalidate
     * @return ResponseEntity with logout confirmation message
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        // Invalidate the session - this removes all session data and makes session unusable
        session.invalidate();
        
        // Return success message
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    /**
     * GET /api/session - Check current session and retrieve user info
     * 
     * This endpoint allows the frontend to:
     * 1. Verify if user has active session
     * 2. Retrieve current user information
     * 3. Handle session timeouts gracefully
     * 
     * Used for maintaining login state across page refreshes and browser sessions.
     * 
     * @param session Current HTTP session to check
     * @return ResponseEntity with user info if session is active, or error if not authenticated
     */
    @GetMapping("/session")
    public ResponseEntity<?> getSession(HttpSession session) {
        // Retrieve user from session (returns null if no session or session expired)
        User user = (User) session.getAttribute("user");
        
        // STEP 1: Check if user is authenticated (has active session)
        if (user != null) {
            // STEP 2: Return current user information
            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("role", user.getRole()); // Frontend needs this for role-based UI
            response.put("sessionId", session.getId()); // For debugging/tracking
            return ResponseEntity.ok(response);
        }
        
        // STEP 3: Return error for no active session
        return ResponseEntity.status(401).body(Map.of("error", "No active session"));
    }
}
