package com.dblab.controller;

import com.dblab.domain.Project;
import com.dblab.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping
    public String projectView(){
        return "/project";
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveProject(@RequestBody Project project){
        projectService.saveProject(project);
        return new ResponseEntity<>("{}", HttpStatus.CREATED);
    }

    @PutMapping("/modify/{idx}")
    public ResponseEntity<?> modifyProject(@PathVariable("idx") Long idx, @RequestBody Project project){
        projectService.modifyProject(idx, project);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{idx}")
    public ResponseEntity<?> deleteProject(@PathVariable("idx") Long idx){
        projectService.deleteProject(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}
