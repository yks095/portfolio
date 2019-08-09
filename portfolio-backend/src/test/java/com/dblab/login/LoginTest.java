package com.dblab.login;

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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LoginTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUserName");
        userDTO.setPassword("testUserPassword");
        userDTO.setEmail("test@gmail.com");

        userService.passwordEncodeAndSave(userDTO);
    }

    @Test
    public void loginTest() throws Exception {
        //로그인
        mockMvc.perform(post("/login").param("username", "testUserName").param("password", "testUserPassword")
                .with(csrf())).andExpect(forwardedUrl("/login/success")).andExpect(authenticated()).andExpect(status().is2xxSuccessful());
    }
}