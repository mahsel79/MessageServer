package se.gritacademy.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import se.gritacademy.util.LoggerUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request) {
        
        String ipAddress = getClientIp(request);
        
        // TODO: Add actual authentication logic
        boolean loginSuccess = true; // Placeholder
        
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
}
