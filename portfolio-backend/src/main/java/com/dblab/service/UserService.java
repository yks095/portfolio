package com.dblab.service;

import com.dblab.domain.User;
import com.dblab.dto.UserDto;
import com.dblab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public void passwordEncodeAndSave(UserDto userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userDTO.setUser());
    }

    public User currentUser(org.springframework.security.core.userdetails.User user) {
        return userRepository.findByUsername(user.getUsername());
    }
}
