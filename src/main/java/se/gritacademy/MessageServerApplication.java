package se.gritacademy;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.*;
import java.util.*;
import java.util.Optional;

@SpringBootApplication
public class MessageServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageServerApplication.class, args);
    }
}

@Entity
class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String role = "user";

    public UserInfo() {}
    public UserInfo(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

}

@Entity
class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}

interface UserRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByEmail(String email);
    List<UserInfo> findAll();
}

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String email, @RequestParam String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }
        userRepository.save(new UserInfo(email, password, "user"));
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        Optional<UserInfo> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            UserInfo user = userOpt.get();
            if (user.getPassword().equals(password)) {
                String token = generateJwtToken(user);
                return ResponseEntity.ok(token);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages(@RequestHeader("Authorization") String token) {
        Claims claims = parseJwtToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(null);
    }

    @GetMapping("/users")
        public ResponseEntity<List<String>> getUsers(@RequestHeader("Authorization") String token) {
        parseJwtToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(null);
    }

    @PostMapping("/messages")
    public ResponseEntity<String> sendMessage(@RequestHeader("Authorization") String token, @RequestParam String recipient, @RequestParam String message) {
        Claims claims = parseJwtToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok("Not implemented yet");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }

    private String generateJwtToken(UserInfo user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .compact();
    }

    private Claims parseJwtToken(String token) {
        return Jwts.parserBuilder()
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

}
