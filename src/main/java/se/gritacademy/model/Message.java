package se.gritacademy.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;
    private String recipient;
    private String message;
    private Date timestamp;

    public Message() {}

    public Message(String sender, String recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.timestamp = new Date();
    }

    public Long getId() { return id; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public Date getTimestamp() { return timestamp; }
}