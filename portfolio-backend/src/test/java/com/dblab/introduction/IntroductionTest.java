package com.dblab.introduction;

import com.dblab.domain.Introduction;
import com.dblab.repository.IntroductionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntroductionTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    IntroductionRepository introductionRepository;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void saveIntroductionTest() throws Exception {
        Introduction introduction = new Introduction();
        introduction.setTitle("Test Title");
        introduction.setGrowth("Test Growth");
        String jsonRequest = objectMapper.writeValueAsString(introduction);

        mockMvc.perform(post("/introduction/save").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        //저장 확인 전
        assertThat(introduction).isNotNull();
        assertThat(introduction.getTitle()).isEqualTo("Test Title");
        assertThat(introduction.getGrowth()).isEqualTo("Test Growth");

        //저장 확인
        introduction = introductionRepository.findByIdx(1L);
        assertThat(introduction).isNotNull();
        assertThat(introduction.getTitle()).isEqualTo("Test Title");
        assertThat(introduction.getGrowth()).isEqualTo("Test Growth");
        assertThat(introduction.getReason()).isNull();
    }

    @Test
    public void modifyIntroductionTest() throws Exception {
        Introduction introduction = new Introduction();
        introduction.setTitle("Test Title");
        introduction.setGrowth("Test Growth");

        mockMvc.perform(post("/introduction/save").content(objectMapper.writeValueAsString(introduction)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        //수정 확인 전
        introduction = introductionRepository.findByIdx(1L);
        assertThat(introduction).isNotNull();
        assertThat(introduction.getTitle()).isEqualTo("Test Title");
        assertThat(introduction.getGrowth()).isEqualTo("Test Growth");

        //수정
        introduction.setTitle("Modify Test");
        introduction.setGrowth("Modify Growth");

        mockMvc.perform(put(("/introduction/modify/1")).content(objectMapper.writeValueAsString(introduction)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        //수정 확인
        introduction = introductionRepository.findByIdx(1L);
        assertThat(introduction).isNotNull();
        assertThat(introduction.getTitle()).isEqualTo("Modify Test");
        assertThat(introduction.getGrowth()).isEqualTo("Modify Growth");
    }

    @Test
    public void deleteIntroductionTest() throws Exception {

        Introduction introduction = new Introduction();
        introduction.setTitle("Test Title");
        introduction.setGrowth("Test Growth");

        mockMvc.perform(post("/introduction/save").content(objectMapper.writeValueAsString(introduction)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        //삭제 확인 전
        introduction = introductionRepository.findByIdx(1L);
        assertThat(introduction).isNotNull();
        assertThat(introduction.getTitle()).isEqualTo("Test Title");
        assertThat(introduction.getGrowth()).isEqualTo("Test Growth");

        //삭제
        mockMvc.perform(delete("/introduction/delete/1")).andExpect(status().isOk());

        //삭제 확인
        introduction = introductionRepository.findByIdx(1L);
        assertThat(introduction).isNull();

    }
}
