package com.dblab.service;

import com.dblab.domain.Project;
import com.dblab.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    public void saveProject(Project project) {
        project.setRegisteredDate(LocalDateTime.now());

        projectRepository.save(project);
    }

    public void modifyProject(Long idx, Project project) {
        Project modifiedProject= projectRepository.findByIdx(idx);
        modifiedProject.modifyProject(project);

        projectRepository.save(modifiedProject);
    }

    public void deleteProject(Long idx) {
        projectRepository.deleteById(idx);
    }

}
