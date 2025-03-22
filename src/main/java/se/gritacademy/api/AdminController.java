package se.gritacademy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.service.UserService;
import se.gritacademy.util.JwtUtil;
import se.gritacademy.util.LoggerUtil;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserService userService;

    /** Block/Unblock a user (Admin only) */
    private final JwtUtil jwtUtil;

    @Autowired
    public AdminController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/block")
    public ResponseEntity<String> blockUser(@RequestHeader("Authorization") String token,
                                            @RequestParam String email,
                                            @RequestParam boolean block) {
        Claims claims = jwtUtil.validateToken(token);
        if (claims == null || !claims.get("role", String.class).equals("admin")) {
            LoggerUtil.log("Unauthorized block/unblock attempt");
            return ResponseEntity.status(403).body("Access denied");
        }

        boolean success = userService.toggleBlockUser(email, block);
        if (success) {
            LoggerUtil.log("Admin " + claims.getSubject() + " " + (block ? "blocked" : "unblocked") + " user: " + email);
            return ResponseEntity.ok(block ? "User blocked" : "User unblocked");
        } else {
            LoggerUtil.log("Failed block/unblock attempt for user: " + email);
            return ResponseEntity.status(404).body("User not found");
        }
    }
}
