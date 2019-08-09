package com.dblab.dto;

import com.dblab.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class UserDTO {

    private String username;

    private String password;

    private String email;

    public User setUser() {
        User user = new User();
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setEmail(this.email);
        user.setRegisteredDate(LocalDateTime.now());
        return user;
    }
}
