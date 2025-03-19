package se.gritacademy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.gritacademy.model.Message;
import se.gritacademy.repository.MessageRepository;
import se.gritacademy.util.EncryptionUtil;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    /** Get messages for a user (decrypting them) */
    public List<Message> getUserMessages(String userEmail) {
        List<Message> messages = messageRepository.findByRecipient(userEmail);
        messages.forEach(msg -> msg.setMessage(EncryptionUtil.decrypt(msg.getMessage())));
        return messages;
    }

    /** Encrypt message before saving */
    public void sendMessage(String sender, String receiver, String content) {
        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(receiver);
        message.setMessage(EncryptionUtil.encrypt(content));  // Encrypt content
        message.setTimestamp(new Date());
        messageRepository.save(message);
    }
}
