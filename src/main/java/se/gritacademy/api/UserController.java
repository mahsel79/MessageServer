package se.gritacademy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.service.UserService;
import se.gritacademy.util.JwtUtil;
import io.jsonwebtoken.Claims;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllUsers(@RequestHeader("Authorization") String token) {
        Claims claims = jwtUtil.validateToken(token);
        if (claims == null) {
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.ok(userService.getAllUserEmails());
    }
}
