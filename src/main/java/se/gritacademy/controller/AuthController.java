package se.gritacademy.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import se.gritacademy.util.LoggerUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    /** Logout (Logs when users log out) */
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        LoggerUtil.log("User logged out: " + token);
        return ResponseEntity.ok("Logged out successfully");
    }
}
