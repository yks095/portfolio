package com.dblab.controller;

import com.dblab.domain.Project;
import com.dblab.domain.User;
import com.dblab.dto.ProjectDto;
import com.dblab.repository.ProjectRepository;
import com.dblab.service.ProjectService;
import com.dblab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/project")
public class ProjectRestController {

    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

    @Autowired
    ProjectRepository projectRepository;

    private User currentUser;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> projectView(@PageableDefault Pageable pageable){

        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        currentUser = userService.currentUser(user);

        Page<Project> projects = projectRepository.findAllByUser(pageable, currentUser);
        PageMetadata pageMetadata = new PageMetadata(pageable.getPageSize(), projects.getNumber(), projects.getTotalElements());
        PagedResources<Project> resources = new PagedResources<>(projects.getContent(), pageMetadata);
        resources.add(linkTo(methodOn(ProjectRestController.class).projectView(pageable)).withSelfRel());

        return ResponseEntity.ok(resources);
    }

    @PostMapping
    public ResponseEntity<?> saveProject(@Valid @RequestBody ProjectDto projectDto){
        projectService.saveProject(projectDto, currentUser);
        return new ResponseEntity<>("{}", HttpStatus.CREATED);
    }

    @PutMapping("/{idx}")
    public ResponseEntity<?> modifyProject(@PathVariable("idx") Long idx, @Valid @RequestBody ProjectDto projectDto){
        projectService.modifyProject(idx, projectDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteProject(@PathVariable("idx") Long idx){
        projectService.deleteProject(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}
