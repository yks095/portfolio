package com.dblab.service;

import com.dblab.domain.Project;
import com.dblab.domain.User;
import com.dblab.dto.ProjectDto;
import com.dblab.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

//    public Page<Project> showProject(Pageable pageable) {
//
//        Page<Project> projects = projectRepository.findAll(pageable);
//        PagedResources.PageMetadata pageMetadata = new PagedResources.PageMetadata(pageable.getPageSize(), projects.getNumber(), projects.getTotalElements());
//
//
//    }
}
