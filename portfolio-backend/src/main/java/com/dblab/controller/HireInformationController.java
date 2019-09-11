package com.dblab.controller;

import com.dblab.domain.HireInformation;
import com.dblab.service.HireInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping("/api/hireInfos")
public class HireInformationController {

    @Autowired
    HireInformationService hireInformationService;

    @PostMapping
    public Object getHireInfo(@RequestBody HireInformation hireInformation){
        return hireInformationService.getHireInfo(hireInformation);
    }

}
