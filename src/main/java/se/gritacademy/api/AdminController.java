package se.gritacademy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.service.UserService;
import se.gritacademy.util.JwtUtil;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/block")
    public ResponseEntity<String> blockUser(@RequestHeader("Authorization") String token,
                                            @RequestParam String email,
                                            @RequestParam boolean block) {
        Claims claims = JwtUtil.validateToken(token);
        if (claims == null || !claims.get("role", String.class).equals("admin")) {
            return ResponseEntity.status(403).body("Access denied");
        }

        boolean success = userService.toggleBlockUser(email, block);
        return success
                ? ResponseEntity.ok(block ? "User blocked" : "User unblocked")
                : ResponseEntity.status(404).body("User not found");
    }
}
