package com.dblab.main;

import com.dblab.common.TestDescription;
import com.dblab.config.jwtConfig.JwtTokenUtil;
import com.dblab.domain.User;
import com.dblab.dto.UserDto;
import com.dblab.repository.UserRepository;
import com.dblab.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MainConrollerTest{

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserRepository userRepository;

    private final String userUri = "/api/users";

    /**
     * getMain test
     * 1. 유저 생성
     * 2. jwt 발급
     * 3. jwt 가지고 main 요청
     * 4. username과 hateoas 반환 확인
     *
     */
    @Test
    @TestDescription("정상 요청")
    public void getMain() throws Exception {
        User currentUser = createUserAndRegister(1);

        mockMvc.perform(get("/api/main")
        .header(HttpHeaders.AUTHORIZATION, getBearerToken(currentUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.introductions").exists())
                .andExpect(jsonPath("_links.projects").exists())
                .andExpect(jsonPath("_links.licenses").exists())
        ;
    }

    @Test
    @TestDescription("인증되지 않은 사용자")
    public void getMainUnAuthenticated() throws Exception{
        mockMvc.perform(get("/api/main"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }

    private String getBearerToken(User currentUser) {
        return "Bearer " + jwtTokenUtil.generateToken(currentUser);
    }

    public User createUserAndRegister(int idx) throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("testID" + idx);
        userDto.setPassword("testPassword");
        userDto.setEmail("test@gmail.com");
        userDto.setGitAddr("https://github.com/testId");

        mockMvc.perform(post(userUri)
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        User user = userRepository.findByUsername(userDto.getUsername());
        return user;
    }
}
