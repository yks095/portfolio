package com.dblab.controller;

import com.dblab.common.CurrentUser;
import com.dblab.controller.resource.MainResource;
import com.dblab.domain.User;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/main", produces = MediaTypes.HAL_JSON_VALUE)
public class MainController {

    @GetMapping
    public ResponseEntity getMain(@CurrentUser User user){
        MainResource mainResource = new MainResource(user);
        mainResource.add(linkTo(IntroductionRestController.class).withRel("introductions"));
        mainResource.add(linkTo(ProjectRestController.class).withRel("projects"));
        mainResource.add(linkTo(LicenseRestController.class).withRel("licenses"));
        return ResponseEntity.ok(mainResource);
    }
}
