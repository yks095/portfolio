package com.dblab.repository;

import com.dblab.domain.Project;
import com.dblab.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByIdx(Long idx);
    Page<Project> findAllByUser(Pageable pageable, User user);
}
