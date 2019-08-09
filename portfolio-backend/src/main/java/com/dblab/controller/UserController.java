package com.dblab.controller;

import com.dblab.domain.User;
import com.dblab.dto.UserDTO;
import com.dblab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO){
        userService.passwordEncodeAndSave(userDTO);

        return new ResponseEntity<>("{}", HttpStatus.CREATED);
    }

}
