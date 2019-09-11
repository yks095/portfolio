package com.dblab.repository;

import com.dblab.domain.License;
import com.dblab.domain.Project;
import com.dblab.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<License, Long> {


    Page<License> findAllByUser(Pageable pageable, User currentUser);

    License findByIdx(Long idx);
}
