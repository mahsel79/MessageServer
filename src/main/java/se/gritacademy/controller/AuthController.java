package se.gritacademy.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import se.gritacademy.util.LoggerUtil;
import se.gritacademy.model.UserInfo;
import se.gritacademy.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request) {
        
        String ipAddress = getClientIp(request);
        
        boolean loginSuccess = userService.authenticateUser(username, password).isPresent();
        
        if (loginSuccess) {
            LoggerUtil.logSuccessfulLogin(username, ipAddress);
            return ResponseEntity.ok("Login successful");
        } else {
            LoggerUtil.logFailedLogin(username, ipAddress);
            return ResponseEntity.badRequest().body("Invalid credentials");
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
