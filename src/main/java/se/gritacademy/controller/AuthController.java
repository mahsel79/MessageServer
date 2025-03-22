package se.gritacademy.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import se.gritacademy.util.LoggerUtil;
import se.gritacademy.model.UserInfo;
import se.gritacademy.model.LoginRequest;
import se.gritacademy.service.UserService;
import se.gritacademy.util.JwtUtil;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {
        
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();
            
            if (email == null || password == null) {
                return ResponseEntity.badRequest().body("Email and password are required");
            }
            
            String ipAddress = getClientIp(request);
            
            Optional<UserInfo> user = userService.authenticateUser(email, password);
            if (user.isPresent()) {
                LoggerUtil.logSuccessfulLogin(email, ipAddress);
                String token = jwtUtil.generateToken(email);
                return ResponseEntity.ok(token);
            } else {
                LoggerUtil.logFailedLogin(email, ipAddress);
                return ResponseEntity.status(401).body("Invalid email or password");
            }
        } catch (Exception e) {
            LoggerUtil.log("Login error: " + e.getMessage());
            return ResponseEntity.status(500).body("An error occurred during login");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader("Authorization") String token,
            @RequestParam String username,
            HttpServletRequest request) {
        
        LoggerUtil.logUserLogout(username);
        return ResponseEntity.ok("Logged out successfully");
    }

    private String getClientIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        if (request.getHeader("X-Forwarded-For") != null) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        }
        return remoteAddr;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserInfo userInfo) {
        // Password validation
        String password = userInfo.getPassword();
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{12,}$";
        
        if (!password.matches(passwordPattern)) {
            return ResponseEntity.badRequest().body("Password must be at least 12 characters long and contain: " +
                    "1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character");
        }

        try {
            userService.registerUser(userInfo);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
