package com.dblab.controller;

import com.dblab.dto.UserDto;
import com.dblab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserDto userDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()) return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
        else {
            userService.passwordEncodeAndSave(userDTO);
            return new ResponseEntity<>("{}", HttpStatus.CREATED);
        }
    }

}
