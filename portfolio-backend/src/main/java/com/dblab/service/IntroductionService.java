package com.dblab.service;

import com.dblab.domain.Introduction;
import com.dblab.repository.IntroductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class IntroductionService {

    @Autowired
    IntroductionRepository introductionRepository;

    public void saveIntroduction(Introduction introduction) {
        introduction.setRegisteredDate(LocalDateTime.now());
        introductionRepository.save(introduction);
    }

    public void modifyIntroduction(Long idx, Introduction introduction) {
        Introduction modifiedIntroduction = introductionRepository.findByIdx(idx);
        modifiedIntroduction.modifyIntroduction(introduction);

        introductionRepository.save(modifiedIntroduction);
    }

    public void deleteIntroduction(Long idx) {
        introductionRepository.deleteById(idx);
    }
}
