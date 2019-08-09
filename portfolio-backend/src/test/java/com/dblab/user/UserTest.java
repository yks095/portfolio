package com.dblab.user;

import com.dblab.domain.User;
import com.dblab.dto.UserDTO;
import com.dblab.repository.UserRepository;
import com.dblab.service.UserService;
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
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testId");
        userDTO.setPassword("testPassword");
        userDTO.setEmail("test@gmail.com");


        //유저 등록
        mockMvc.perform(post("/user/save").content(mapper.writeValueAsString(userDTO)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        //등록 확인
        User user = userRepository.findByIdx(1L);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testId");
        assertThat(user.getEmail()).isEqualTo("test@gmail.com");
    }
}
