package com.dblab.controller;

import com.dblab.domain.Introduction;
import com.dblab.domain.User;
import com.dblab.dto.IntroductionDto;
import com.dblab.service.IntroductionService;
import com.dblab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.PagedResources.PageMetadata;

@RestController
@RequestMapping(value = "/api/introductions")
public class IntroductionRestController {

    @Autowired
    private IntroductionService introductionService;

    @Autowired
    private UserService userService;

    private User currentUser;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIntroductions(@PageableDefault Pageable pageable){
        org.springframework.security.core.userdetails.User user
                = (org.springframework.security.core.userdetails.User)
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        currentUser = userService.currentUser(user);

        Page<Introduction> introductions = introductionService.findIntroductions(currentUser, pageable);
        PageMetadata pageMetadata = new PageMetadata(pageable.getPageSize(),
                                            introductions.getNumber(), introductions.getTotalElements());

        PagedResources<Introduction> resources = new PagedResources<>(
                                                    introductions.getContent(), pageMetadata);

        //수정 필요
        resources.add(new Link("localhost8080:/api/introductions").withSelfRel());

        return ResponseEntity.ok(resources);
    }

    @PostMapping
    public ResponseEntity<?> postIntroductions(@Valid  @RequestBody IntroductionDto introductionDto,
                                               BindingResult bindingResult){
        if(bindingResult.hasErrors()) return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
        else {
            introductionService.saveIntroduction(introductionDto, currentUser);
            return new ResponseEntity<>("{}", HttpStatus.CREATED);
        }
    }

    @PutMapping("/{idx}")
    public ResponseEntity<?> putIntroductions(@PathVariable("idx") Long idx,
                                              @Valid @RequestBody IntroductionDto introductionDto,
                                              BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
        }
        else{
            introductionService.modifyIntroduction(idx, introductionDto);
            return new ResponseEntity<>("{}", HttpStatus.OK);
        }
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteIntroductions(@PathVariable("idx") Long idx){
        introductionService.deleteIntroduction(idx);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

}
