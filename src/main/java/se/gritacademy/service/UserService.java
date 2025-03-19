package se.gritacademy.service;

import se.gritacademy.model.UserInfo;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<String> getAllUserEmails();
    boolean toggleBlockUser(String email, boolean block);
    Optional<UserInfo> authenticateUser(String email, String password);
    void registerUser(UserInfo userInfo);
}
