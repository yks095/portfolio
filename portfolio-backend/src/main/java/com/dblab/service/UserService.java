package com.dblab.service;

import com.dblab.domain.User;
import com.dblab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public void passwordEncodeAndSave(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegisteredDate(LocalDateTime.now());

        userRepository.save(user);
    }

}
