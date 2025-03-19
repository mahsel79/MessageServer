package se.gritacademy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.gritacademy.model.Message;
import se.gritacademy.service.MessageService;
import se.gritacademy.util.JwtUtil;
import io.jsonwebtoken.Claims;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    public ResponseEntity<List<Message>> getMessages(@RequestHeader("Authorization") String token) {
        Claims claims = JwtUtil.validateToken(token);
        if (claims == null) {
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.ok(messageService.getUserMessages(claims.getSubject()));
    }

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestHeader("Authorization") String token,
                                              @RequestParam String recipient,
                                              @RequestParam String message) {
        Claims claims = JwtUtil.validateToken(token);
        if (claims == null) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
        messageService.sendMessage(claims.getSubject(), recipient, message);
        return ResponseEntity.ok("Message sent successfully");
    }
}
