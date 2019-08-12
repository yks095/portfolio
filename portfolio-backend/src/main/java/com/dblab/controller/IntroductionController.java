package com.dblab.controller;

import com.dblab.domain.Introduction;
import com.dblab.domain.User;
import com.dblab.service.IntroductionService;
import com.dblab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/introduction")
public class IntroductionController {

    @Autowired
    IntroductionService introductionService;

    @Autowired
    UserService userService;

    private User currentUser;

    @GetMapping
    public String introductionView(){
        org.springframework.security.core.userdetails.User user
                = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        currentUser = userService.currentUser(user);

        return "/introduction";
    }

    @PostMapping
    public ResponseEntity<?> saveIntroduction(@RequestBody Introduction introduction){
        //유저와 매핑
        introduction.setUsers(currentUser);

        introductionService.saveIntroduction(introduction);
        return new ResponseEntity<>("{}", HttpStatus.CREATED);
    }

    @PutMapping("/{idx}")
    public ResponseEntity<?> modifyIntroduction(@PathVariable("idx") Long idx, @RequestBody Introduction introduction){
        introductionService.modifyIntroduction(idx, introduction);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteIntroduction(@PathVariable("idx") Long idx){
        introductionService.deleteIntroduction(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

}
