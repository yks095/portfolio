package com.dblab.controller;

import com.dblab.dto.UserDto;
import com.dblab.service.FileService;
import com.dblab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    FileService fileService;

    @GetMapping({"/{fileName:.+}", "/{idx}/{fileName:.+}"})
    public ResponseEntity<Resource> getFile(@PathVariable String fileName,
                                            @PathVariable(required = false) Long idx,
                                            HttpServletRequest request){

        return  fileService.loadFileAsResource(fileName, request);
    }

    @PostMapping
    public ResponseEntity<?> saveUser(@Valid UserDto userDTO,
                                      Errors errors,
                                      @RequestParam(value = "file", required = false) MultipartFile file,
                                      HttpServletRequest request){

        if(errors.hasErrors())
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        else {
            if(userService.userRedunduncyCheck(userDTO)){
                return new ResponseEntity<>("중복된 ID", HttpStatus.BAD_REQUEST);
            }
            else {
                if(file == null)    {
                    String uri = fileService.setDefaultImage(request);
                    userService.passwordEncodeAndSave(userDTO, uri);
                }
                else    {
                    String uri = fileService.storeFile(file, request);
                    userService.passwordEncodeAndSave(userDTO, uri);
                }
                return new ResponseEntity<>("{}", HttpStatus.CREATED);
            }
        }
    }

    @PutMapping("/{idx}")
    public ResponseEntity<?> modifyProject(@PathVariable("idx") Long idx,
                                           @Valid UserDto userDto,
                                           @RequestParam(value = "file", required = false) MultipartFile file,
                                           Errors errors,
                                           HttpServletRequest request){
        if(errors.hasErrors())
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        else {
            if(file == null)    {
                String uri = fileService.setDefaultImage(request);
                userService.modifyUser(idx, userDto, uri);
            }
            else    {
                String uri = fileService.storeFile(file, request);
                userService.modifyUser(idx, userDto, uri);
            }
            return new ResponseEntity<>("{}", HttpStatus.OK);
        }
    }

}