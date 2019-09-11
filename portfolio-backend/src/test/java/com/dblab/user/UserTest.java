package com.dblab.user;

import com.dblab.domain.User;
import com.dblab.dto.UserDto;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void saveUserProfileTest() throws Exception {

        String url = "test_url";

        UserDto userDTO = new UserDto();
        userDTO.setUsername("testId");
        userDTO.setPassword("testPassword");
        userDTO.setEmail("test@gmail.com");

        //유저 등록
        mockMvc.perform(post("/user").content(mapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        //데이터베이스 확인
        User user = userRepository.findByIdx(1L);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testId");
        assertThat(user.getEmail()).isEqualTo("test@gmail.com");

        // 현재 유저 이미지 등록
        userService.uploadImage(user, url);

        // 유저 저장
        mockMvc.perform(post("/user/upload").content(mapper.writeValueAsString(userDTO))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());

        // 데이터베이스 확인
        User user2 = userRepository.findByIdx(1L);
        assertThat(user2).isNotNull();
        assertThat(user2.getUsername()).isEqualTo("testId");
        assertThat(user2.getEmail()).isEqualTo("test@gmail.com");
        assertThat(user2.getProfile()).isEqualTo("test_url");

    }

    @Test
    public void saveUserTest() throws Exception {
        UserDto userDTO = new UserDto();
        userDTO.setUsername("testId");
        userDTO.setPassword("testPassword");
        userDTO.setEmail("test@gmail.com");

        //유저 등록
        mockMvc.perform(post("/user").content(mapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        //데이터베이스 확인
        User user = userRepository.findByIdx(1L);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testId");
        assertThat(user.getEmail()).isEqualTo("test@gmail.com");

        //아이디가 4자 미만일 경우
        userDTO.setUsername("id");

        //유저 등록
        mockMvc.perform(post("/user").content(mapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        //데이터베이스 확인
        user = userRepository.findByIdx(2L);
        assertThat(user).isNull();

        //아이디가 12자 초과일 경우
        userDTO.setUsername("testUserName12");

        //유저 등록
        mockMvc.perform(post("/user").content(mapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        //데이터 베이스 확인
        user = userRepository.findByIdx(2L);
        assertThat(user).isNull();

        //패스워드가 6자 미만일 경우
        userDTO.setUsername("testId");
        userDTO.setPassword("passw");

        //유저 등록
        mockMvc.perform(post("/user").content(mapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        //데이터 베이스 확인
        user = userRepository.findByIdx(2L);
        assertThat(user).isNull();

        //패스워드가 16자 초과일 경우
        userDTO.setPassword("testUserPassword12");

        //유저 등록
        mockMvc.perform(post("/user").content(mapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        //데이터 베이스 확인
        user = userRepository.findByIdx(2L);
        assertThat(user).isNull();

        //이메일 형식이 틀렸을 경우
        userDTO.setPassword("testPassword");
        userDTO.setEmail("test");

        //유저 등록
        mockMvc.perform(post("/user").content(mapper.writeValueAsString(userDTO)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        //데이터 베이스 확인
        user = userRepository.findByIdx(2L);
        assertThat(user).isNull();
    }

}
