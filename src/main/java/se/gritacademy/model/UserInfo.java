package se.gritacademy.model;

import jakarta.persistence.*;

@Entity
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String role = "user";
    private boolean blocked = false;  // New field

    public UserInfo() {}

    public UserInfo(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.blocked = false;  // Default: User is not blocked
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public boolean isBlocked() { return blocked; }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
