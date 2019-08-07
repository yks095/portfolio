package com.dblab.repository;

import com.dblab.domain.Introduction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntroductionRepository extends JpaRepository<Introduction, Long> {
    Introduction findByIdx(Long idx);
}
