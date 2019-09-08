package com.dblab.controller;

import com.dblab.domain.User;
import com.dblab.dto.UserDto;
import com.dblab.service.UserService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    private User currentUser;

    @PostMapping
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserDto userDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
        else {
            userService.passwordEncodeAndSave(userDTO);
            return new ResponseEntity<>("{}", HttpStatus.CREATED);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadAttachment(@RequestPart("sourceFile") MultipartFile sourceFile) throws IOException {

        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        currentUser = userService.currentUser(user);

        String sourceFileName = sourceFile.getOriginalFilename();
        String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();

        File destinationFile;
        String destinationFileName;
        do {
            destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
            destinationFile = new File("C:/Users/Yang/Desktop/images/" + destinationFileName);
        } while (destinationFile.exists());
        destinationFile.getParentFile().mkdirs();
        sourceFile.transferTo(destinationFile);

        UploadAttachmentResponse response = new UploadAttachmentResponse();
        response.setFileName(sourceFile.getOriginalFilename());
        response.setFileSize(sourceFile.getSize());
        response.setFileContentType(sourceFile.getContentType());
        response.setAttachmentUrl("http://localhost:8080/user/upload/" + destinationFileName);

        String url = response.getAttachmentUrl();

        userService.uploadImage(currentUser, url);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @NoArgsConstructor
    @Data
    private static class UploadAttachmentResponse {

        private String fileName;

        private long fileSize;

        private String fileContentType;

        private String attachmentUrl;
    }

}
