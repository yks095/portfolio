package com.dblab.service;

import com.dblab.domain.Introduction;
import com.dblab.domain.User;
import com.dblab.dto.IntroductionDto;
import com.dblab.repository.IntroductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IntroductionService {

    @Autowired
    private IntroductionRepository introductionRepository;

    public void saveIntroduction(IntroductionDto introductionDto, User currentUser) {


        introductionRepository.save(introductionDto.setIntroduction(currentUser));
    }

    public void modifyIntroduction(Long idx, IntroductionDto introductionDto) {
        Introduction modifiedIntroduction = introductionRepository.findByIdx(idx);
        modifiedIntroduction.modifyIntroduction(introductionDto);

        introductionRepository.save(modifiedIntroduction);
    }

    public void deleteIntroduction(Long idx) {
        introductionRepository.deleteById(idx);
    }

    public Page<Introduction> findIntroductions(User currentUser, Pageable pageable) {
        return introductionRepository.findAllByUser(currentUser, pageable);
    }
}
