package se.gritacademy.controller;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import se.gritacademy.model.UserInfo;
import se.gritacademy.model.Message;
import se.gritacademy.repository.UserRepository;
import se.gritacademy.repository.MessageRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final String SECRET_KEY = "mySuperSecretKeyForJWTGenerationMustBeAtLeast256Bits";  // Byt till en säkrare nyckel
    private static final long EXPIRATION_TIME = 86400000; // 1 dag i millisekunder (24h)
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    /** User Registration */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String email, @RequestParam String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }
        String hashedPassword = passwordEncoder.encode(password);
        userRepository.save(new UserInfo(email, hashedPassword, "user"));
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    /** User Login */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        Optional<UserInfo> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            UserInfo user = userOpt.get();

            if (user.isBlocked()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is blocked.");
            }

            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = generateJwtToken(user);
                return ResponseEntity.ok(token);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }


    /** Get List of Users */
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestHeader("Authorization") String authHeader) {
        Claims claims = validateToken(authHeader);
        if (claims == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
        List<String> emails = userRepository.findAll()
                .stream()
                .map(UserInfo::getEmail)
                .collect(Collectors.toList());
        return ResponseEntity.ok(emails);
    }

    /** Get Messages for Logged-in User */
    @GetMapping("/messages")
    public ResponseEntity<?> getMessages(@RequestHeader("Authorization") String authHeader) {
        Claims claims = validateToken(authHeader);
        if (claims == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
        String email = claims.getSubject();
        return ResponseEntity.ok(messageRepository.findByRecipient(email));
    }

    /** Send Message to Another User */
    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestHeader("Authorization") String authHeader,
                                         @RequestParam String recipient,
                                         @RequestParam String message) {
        Claims claims = validateToken(authHeader);
        if (claims == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        String sender = claims.getSubject();
        if (userRepository.findByEmail(recipient).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipient not found");
        }

        Message newMessage = new Message(sender, recipient, message);
        messageRepository.save(newMessage);
        return ResponseEntity.ok("Message sent");
    }

    /** Logout (Placeholder) */
    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }

    /** JWT Token Generation */
    private String generateJwtToken(UserInfo user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** JWT Token Validation */
    private Claims validateToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        try {
            String token = authHeader.replace("Bearer ", "");
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            return null;
        }
    }
}
