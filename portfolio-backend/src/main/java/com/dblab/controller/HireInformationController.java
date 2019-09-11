package com.dblab.controller;

import com.dblab.domain.HireInformation;
import com.dblab.service.HireInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
