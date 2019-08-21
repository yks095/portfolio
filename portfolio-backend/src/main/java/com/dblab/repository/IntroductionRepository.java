package com.dblab.repository;

import com.dblab.domain.Introduction;
import com.dblab.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroductionRepository extends JpaRepository<Introduction, Long> {
    Introduction findByIdx(Long idx);
    Page<Introduction> findAllByUser(User user, Pageable pageable);
}
