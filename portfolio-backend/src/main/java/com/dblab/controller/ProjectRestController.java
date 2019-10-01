package com.dblab.controller;

import com.dblab.domain.User;
import com.dblab.dto.ProjectDto;
import com.dblab.repository.ProjectRepository;
import com.dblab.service.FileService;
import com.dblab.service.ProjectService;
import com.dblab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/projects")
public class ProjectRestController {

    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

    @Autowired
    FileService fileService;

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

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName, HttpServletRequest request){

        return  fileService.loadFileAsResource(fileName, request);
    }

    @PostMapping
    public ResponseEntity<?> saveProject(@Valid ProjectDto projectDto,
                                         BindingResult bindingResult,
                                         @RequestParam(value = "file", required = false) MultipartFile file,
                                         HttpServletRequest request) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        else {

            if(file == null)    {
                String uri = fileService.setDefaultImage(request);
                projectService.saveProject(projectDto, currentUser, uri);
            }
            else    {
                String uri = fileService.storeFile(file, request);
                projectService.saveProject(projectDto, currentUser, uri);
            }
            return new ResponseEntity<>("{}", HttpStatus.CREATED);
        }
    }

    @PutMapping("/{idx}")
    public ResponseEntity<?> modifyProject(@PathVariable("idx") Long idx,
                                           @Valid ProjectDto projectDto,
                                           @RequestParam(value = "file", required = false) MultipartFile file,
                                           BindingResult bindingResult,
                                           HttpServletRequest request){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        else {
            if(file == null)    {
                String uri = fileService.setDefaultImage(request);
                projectService.modifyProject(idx, projectDto, uri);
            }
            else    {
                String uri = fileService.storeFile(file, request);
                projectService.modifyProject(idx, projectDto, uri);
            }
            return new ResponseEntity<>("{}", HttpStatus.OK);
        }
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteProject(@PathVariable("idx") Long idx){
        projectService.deleteProject(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}
