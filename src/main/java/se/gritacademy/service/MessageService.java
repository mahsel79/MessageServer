package se.gritacademy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.gritacademy.model.Message;
import se.gritacademy.repository.MessageRepository;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getUserMessages(String userEmail) {
        return messageRepository.findByRecipient(userEmail);
    }

    public void sendMessage(String sender, String receiver, String content) {
        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(receiver);
        message.setMessage(content);
        message.setTimestamp(new Date());  // Store message with current timestamp
        messageRepository.save(message);
    }
}
