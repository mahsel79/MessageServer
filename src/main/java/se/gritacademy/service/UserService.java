package se.gritacademy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.gritacademy.model.UserInfo;
import se.gritacademy.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /** Retrieve all user emails */
    public List<String> getAllUserEmails() {
        return userRepository.findAll().stream().map(UserInfo::getEmail).toList();
    }

    /** Block or unblock a user */
    public boolean toggleBlockUser(String email, boolean block) {
        Optional<UserInfo> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            UserInfo user = userOptional.get();
            user.setBlocked(block);  // Assuming `UserInfo` has a `blocked` field
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
