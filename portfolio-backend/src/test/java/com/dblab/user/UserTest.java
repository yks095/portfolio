package com.dblab.user;

import com.dblab.domain.User;
import com.dblab.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void saveUserTest() throws Exception {
        User user = new User();
        user.setUsername("testUserName");
        user.setPassword(passwordEncoder.encode("testUserPassword"));
        user.setEmail("testUserEmail@gmail.com");
        user.setRegisteredDate(LocalDateTime.now());

        //유저 등록
        mockMvc.perform(post("/user/save").content(mapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        //등록 확인
        user = userRepository.findByUsername(user.getUsername());
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testUserName");
        assertThat(user.getEmail()).isEqualTo("testUserEmail@gmail.com");
    }
}
