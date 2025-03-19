package se.gritacademy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import se.gritacademy.model.UserInfo;
import se.gritacademy.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /** Register User with Hashed Password */
    public boolean registerUser(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            return false; // Email already exists
        }

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Password must be at least 12 characters long, include an uppercase letter, lowercase letter, digit, and special character.");
        }

        String hashedPassword = passwordEncoder.encode(password);
        userRepository.save(new UserInfo(email, hashedPassword, "user"));
        return true;
    }

    /** Authenticate User */
    public Optional<UserInfo> loginUser(String email, String password) {
        Optional<UserInfo> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return userOpt;
        }
        return Optional.empty();
    }

    /** Retrieve all user emails */
    public List<String> getAllUserEmails() {
        return userRepository.findAll().stream().map(UserInfo::getEmail).toList();
    }

    /** Password Policy Enforcement */
    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{12,}$";
        return Pattern.matches(passwordRegex, password);
    }
}
