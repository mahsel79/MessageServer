package se.gritacademy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.gritacademy.model.UserInfo;
import se.gritacademy.repository.UserRepository;
import se.gritacademy.util.LoggerUtil;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<String> getAllUserEmails() {
        LoggerUtil.log("User list requested.");
        return userRepository.findAll().stream().map(UserInfo::getEmail).toList();
    }

    @Override
    public boolean toggleBlockUser(String email, boolean block) {
        Optional<UserInfo> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            UserInfo user = userOptional.get();
            user.setBlocked(block);
            userRepository.save(user);

            LoggerUtil.log("User " + email + " was " + (block ? "blocked" : "unblocked") + " by an admin.");
            return true;
        }
        LoggerUtil.log("Failed attempt to block/unblock user: " + email);
        return false;
    }

    @Override
    public Optional<UserInfo> authenticateUser(String email, String password) {
        Optional<UserInfo> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            UserInfo user = userOptional.get();
            if (user.getPassword().equals(password)) {
                LoggerUtil.log("Successful login for user: " + email);
                return userOptional;
            }
        }
        LoggerUtil.log("Failed login attempt for email: " + email);
        return Optional.empty();
    }

    @Override
    public void registerUser(UserInfo userInfo) {
        if (userRepository.findByEmail(userInfo.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        userRepository.save(userInfo);
        LoggerUtil.log("New user registered: " + userInfo.getEmail());
    }
}
