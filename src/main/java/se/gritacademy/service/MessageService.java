package se.gritacademy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.gritacademy.model.Message;
import se.gritacademy.repository.MessageRepository;
import java.time.Instant;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getUserMessages(String userEmail) {
        return messageRepository.findByReceiver(userEmail);
    }

    public void sendMessage(String sender, String receiver, String content) {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessage(content);
        message.setDate(Instant.now());  // Store message with UTC timestamp
        messageRepository.save(message);
    }
}
