package com.dblab.jwt;

import com.dblab.common.TestDescription;
import com.dblab.config.jwtConfig.JwtTokenUtil;
import com.dblab.domain.User;
import com.dblab.dto.UserDto;
import com.dblab.repository.UserRepository;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    @TestDescription("sdsdsd")
    public void JWT토큰_반환_확인() throws Exception {

        UserDto userDto = create_user(1);

        User user = userRepository.findByIdx(1L);

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testId1");

        mockMvc.perform(post("/login/authenticate")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                        .andExpect(status().isOk())
                        .andDo(print());

        //인증되지 않은 사용자
        userDto.setUsername("testId2");

        mockMvc.perform(post("/login/authenticate")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().is4xxClientError()) //filter에서 db에 없는 값을
                        .andDo(print());
    }


    @Test
    public void JWT_정상동작_확인() throws Exception {

        //유저 생성
        void_create_user(1);

        User user = userRepository.findByIdx(1L);

        assertThat(user).isNotNull();

        //JWT토큰 생성
        String jwtToken = jwtTokenUtil.generateToken(user);

        assertThat(jwtTokenUtil.usernameFromToken(jwtToken)).isEqualTo("testId1");

        //권한이 필요한 페이지 요청
        mockMvc.perform(get("/api/introductions").header("Authorization", "Bearer "+jwtToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //UserDto 객체를 반환하는 유저 생성 메소드
    public UserDto create_user(int idx) throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("testId" + idx);
        userDto.setPassword("testPassword");
        userDto.setEmail("test@gmail.com");

        mockMvc.perform(post("/user")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isCreated());

        return userDto;
    }

    //반환 값이 없는 유저 생성 메소드
    public void void_create_user(int idx) throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("testId" + idx);
        userDto.setPassword("testPassword");
        userDto.setEmail("test@gmail.com");

        mockMvc.perform(post("/user")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isCreated());

    }
}
