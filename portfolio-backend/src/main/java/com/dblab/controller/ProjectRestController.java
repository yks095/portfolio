package com.dblab.controller;

import com.dblab.domain.User;
import com.dblab.dto.ProjectDto;
import com.dblab.repository.ProjectRepository;
import com.dblab.service.ProjectService;
import com.dblab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/projects")
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

        return ResponseEntity.ok(projectService.getProjects(pageable, currentUser));
    }

    @PostMapping
    public ResponseEntity<?> saveProject(@Valid @RequestBody ProjectDto projectDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
        else {
            projectService.saveProject(projectDto, currentUser);
            return new ResponseEntity<>("{}", HttpStatus.CREATED);
        }
    }

    @PutMapping("/{idx}")
    public ResponseEntity<?> modifyProject(@PathVariable("idx") Long idx, @Valid @RequestBody ProjectDto projectDto, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
        else {
            projectService.modifyProject(idx, projectDto);
            return new ResponseEntity<>("{}", HttpStatus.OK);
        }
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteProject(@PathVariable("idx") Long idx){
        projectService.deleteProject(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}
