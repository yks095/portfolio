package com.dblab.controller.resource;

import com.dblab.controller.MainController;
import com.dblab.domain.User;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class MainResource extends Resource<User> {

    public MainResource(User currentUser, Link... links) {
        super(currentUser, links);
        add(linkTo(MainController.class).withSelfRel());
    }
}
