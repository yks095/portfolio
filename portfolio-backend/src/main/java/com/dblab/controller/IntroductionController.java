package com.dblab.controller;

import com.dblab.domain.Introduction;
import com.dblab.service.IntroductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/introduction")
public class IntroductionController {

    @Autowired
    IntroductionService introductionService;

    @GetMapping
    public String introductionView(){
        return "/introduction";
    }

    @PostMapping
    public ResponseEntity<?> saveIntroduction(@RequestBody Introduction introduction){
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
