package se.gritacademy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.model.Message;
import se.gritacademy.service.MessageService;
import se.gritacademy.util.JwtUtil;
import se.gritacademy.util.LoggerUtil;
import io.jsonwebtoken.Claims;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /** Get messages for logged-in user */
    private final JwtUtil jwtUtil;

    @Autowired
    public MessageController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessages(@RequestHeader("Authorization") String token) {
        Claims claims = jwtUtil.validateToken(token);
        if (claims == null) {
            LoggerUtil.log("Unauthorized attempt to retrieve messages.");
            return ResponseEntity.status(401).body(null);
        }

        LoggerUtil.log("Messages requested for user: " + claims.getSubject());
        return ResponseEntity.ok(messageService.getUserMessages(claims.getSubject()));
    }

    /** Send message to another user */
    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestHeader("Authorization") String token,
                                              @RequestParam String recipient,
                                              @RequestParam String message) {
        Claims claims = jwtUtil.validateToken(token);
        if (claims == null) {
            LoggerUtil.log("Unauthorized attempt to send message.");
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        messageService.sendMessage(claims.getSubject(), recipient, message);
        LoggerUtil.log("User " + claims.getSubject() + " sent a message to " + recipient);
        return ResponseEntity.ok("Message sent successfully");
    }
}
