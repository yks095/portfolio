package com.dblab.introduction;

import com.dblab.domain.Introduction;
import com.dblab.dto.UserDto;
import com.dblab.repository.IntroductionRepository;
import com.dblab.repository.UserRepository;
import com.dblab.service.CustomUserDetailsService;
import com.dblab.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Autowired
    UserService userService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    UserRepository userRepository;

    private UserDetails userDetails;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        //유저 정보 입력
        UserDto userDto = new UserDto();
        userDto.setUsername("testUserName");
        userDto.setPassword("testUserPassword");
        userDto.setEmail("test@gmail.com");

        //유저 등록
        mockMvc.perform(post("/user").content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf()))
                .andExpect(status().isCreated());

        //로그인
        mockMvc.perform(formLogin().user("testUserName").password("testUserPassword"))
                .andExpect(forwardedUrl("/login/success"))
                .andExpect(authenticated());

        //set userDetailis
        userDetails = customUserDetailsService.loadUserByUsername("testUserName");

        //userDetailis 확인
        assertThat(userDetails.getUsername()).isEqualTo("testUserName");

        //현재 유저 매핑
        mockMvc.perform(get("/introduction").with(csrf()).with(user(userDetails)))
                .andExpect(authenticated())
                .andExpect(status().isOk());

    }

    @Test
    public void saveIntroductionTest() throws Exception {
        Introduction introduction = new Introduction();
        introduction.setTitle("Test Title");
        introduction.setGrowth("Test Growth");

        mockMvc.perform(post("/introduction").content(objectMapper.writeValueAsString(introduction))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf()).with(user(userDetails)))
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

        //유저 매핑확인
        assertThat(introduction.getUser()).isNotNull();
        assertThat(introduction.getUser().getUsername()).isEqualTo("testUserName");
        assertThat(introduction.getUser().getEmail()).isEqualTo("test@gmail.com");

    }

    @Test
    public void modifyIntroductionTest() throws Exception {
            Introduction introduction = new Introduction();
            introduction.setTitle("Test Title");
            introduction.setGrowth("Test Growth");

        mockMvc.perform(post("/introduction").content(objectMapper.writeValueAsString(introduction))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf()).with(user(userDetails)))
                .andExpect(status().isCreated());

        //수정 확인 전
        introduction = introductionRepository.findByIdx(1L);
        assertThat(introduction).isNotNull();
        assertThat(introduction.getTitle()).isEqualTo("Test Title");
        assertThat(introduction.getGrowth()).isEqualTo("Test Growth");

        //수정
        introduction.setTitle("Modify Test");
        introduction.setGrowth("Modify Growth");

        mockMvc.perform(put(("/introduction/1")).content(objectMapper.writeValueAsString(introduction))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf()).with(user(userDetails)))
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

        mockMvc.perform(post("/introduction").content(objectMapper.writeValueAsString(introduction))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf()).with(user(userDetails)))
                .andExpect(status().isCreated());

        //삭제 확인 전
        introduction = introductionRepository.findByIdx(1L);
        assertThat(introduction).isNotNull();
        assertThat(introduction.getTitle()).isEqualTo("Test Title");
        assertThat(introduction.getGrowth()).isEqualTo("Test Growth");

        //삭제
        mockMvc.perform(delete("/introduction/1").with(csrf()).with(user(userDetails)))
                .andExpect(status().isOk());

        //삭제 확인
        introduction = introductionRepository.findByIdx(1L);
        assertThat(introduction).isNull();
    }
}
