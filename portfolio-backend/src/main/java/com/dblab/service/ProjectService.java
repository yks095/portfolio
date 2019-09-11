package com.dblab.service;

import com.dblab.controller.ProjectRestController;
import com.dblab.domain.Project;
import com.dblab.domain.User;
import com.dblab.dto.ProjectDto;
import com.dblab.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;


    public void saveProject(ProjectDto projectDto, User user) {
        projectRepository.save(projectDto.save(user));
    }

    public void modifyProject(Long idx, ProjectDto projectDto) {
        Project modifiedProject= projectRepository.findByIdx(idx);
        modifiedProject.modifyProject(projectDto);

        projectRepository.save(modifiedProject);
    }

    public void deleteProject(Long idx) {
        projectRepository.deleteById(idx);
    }

    public PagedResources<Project> getProjects(Pageable pageable, User currentUser) {

        Page<Project> projects = projectRepository.findAllByUser(pageable, currentUser);
        PagedResources.PageMetadata pageMetadata = new PagedResources.PageMetadata(pageable.getPageSize(), projects.getNumber(), projects.getTotalElements());
        PagedResources<Project> resources = new PagedResources<>(projects.getContent(), pageMetadata);
        resources.add(linkTo(methodOn(ProjectRestController.class).projectView(pageable)).withSelfRel());

        return resources;

    }

}
