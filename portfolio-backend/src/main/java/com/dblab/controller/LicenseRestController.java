package com.dblab.controller;

import com.dblab.domain.User;
import com.dblab.dto.LicenseDto;
import com.dblab.repository.LicenseRepository;
import com.dblab.service.LicenseService;
import com.dblab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/licenses")
public class LicenseRestController {

    @Autowired
    UserService userService;

    @Autowired
    LicenseRepository licenseRepository;

    @Autowired
    LicenseService licenseService;

    private User currentUser;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> licenseView(@PageableDefault Pageable pageable) {

        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        currentUser = userService.currentUser(user);

        return ResponseEntity.ok(licenseService.getLicenses(pageable, currentUser));

    }

    @PostMapping
    public ResponseEntity<?> saveLicense(@Valid @RequestBody LicenseDto licenseDto, Errors errors) {
        if (errors.hasErrors())
            return new ResponseEntity<>(errors.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        else {
            licenseService.saveLicense(licenseDto, currentUser);
            return new ResponseEntity<>("{}", HttpStatus.CREATED);
        }
    }

    @PutMapping("/{idx}")
    public ResponseEntity<?> modifyLicense(@PathVariable("idx") Long idx, @Valid @RequestBody LicenseDto licenseDto, Errors errors){
        if(errors.hasErrors())
            return new ResponseEntity<>(errors.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        else    {
            licenseService.modifyLicense(idx, licenseDto);
            return new ResponseEntity<>("{}", HttpStatus.OK);
        }
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteLicense(@PathVariable("idx") Long idx){
        licenseService.deleteLicense(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

}
