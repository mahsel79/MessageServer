package se.gritacademy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.gritacademy.model.UserInfo;
import se.gritacademy.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public boolean registerUser(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            return false; // Email already exists
        }
        userRepository.save(new UserInfo(email, password, "user"));
        return true;
    }

    public Optional<UserInfo> loginUser(String email, String password) {
        Optional<UserInfo> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }
        return Optional.empty();
    }

    public List<String> getAllUserEmails() {
        return userRepository.findAll().stream().map(UserInfo::getEmail).toList();
    }
}
