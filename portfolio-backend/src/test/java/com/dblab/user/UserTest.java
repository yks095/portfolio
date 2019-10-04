package com.dblab.user;

import com.dblab.domain.User;
import com.dblab.dto.UserDto;
import com.dblab.repository.UserRepository;
import com.dblab.service.CustomUserDetailsService;
import com.dblab.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    private final String saveUserURL = "/api/users";

    @Test
    public void saveUserProfileTest() throws Exception {

        String url = "test_url";

        UserDto userDTO = new UserDto();
        userDTO.setUsername("testId");
        userDTO.setPassword("testPassword");
        userDTO.setEmail("test@gmail.com");
        userDTO.setGitAddr("https://github.com/testId");

        //유저 등록
        mockMvc.perform(post("/user").content(mapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        //데이터베이스 확인
        User user = userRepository.findByIdx(1L);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testId");
        assertThat(user.getEmail()).isEqualTo("test@gmail.com");
        assertThat(user.getGitAddr()).isEqualTo("https://github.com/testId");

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
        assertThat(user2.getGitAddr()).isEqualTo("https://github.com/testId");

    }

    @Test
    public void saveUserTest() throws Exception {
        UserDto userDTO = new UserDto();
        userDTO.setUsername("testId");
        userDTO.setPassword("testPassword");
        userDTO.setEmail("test@gmail.com");
        userDTO.setGitAddr("https://github.com/testId");

        //유저 등록
        mockMvc.perform(post(saveUserURL).content(mapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        //데이터베이스 확인
        User user = userRepository.findByIdx(1L);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testId");
        assertThat(user.getEmail()).isEqualTo("test@gmail.com");
        assertThat(user.getGitAddr()).isEqualTo("https://github.com/testId");

        //아이디가 4자 미만일 경우
        userDTO.setUsername("id");

        //유저 등록
        mockMvc.perform(post(saveUserURL).content(mapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        //데이터베이스 확인
        user = userRepository.findByIdx(2L);
        assertThat(user).isNull();

        //아이디가 12자 초과일 경우
        userDTO.setUsername("testUserName12");

        //유저 등록
        mockMvc.perform(post(saveUserURL).content(mapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        //데이터 베이스 확인
        user = userRepository.findByIdx(2L);
        assertThat(user).isNull();

        //패스워드가 6자 미만일 경우
        userDTO.setUsername("testId");
        userDTO.setPassword("passw");

        //유저 등록
        mockMvc.perform(post(saveUserURL).content(mapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        //데이터 베이스 확인
        user = userRepository.findByIdx(2L);
        assertThat(user).isNull();

        //패스워드가 16자 초과일 경우
        userDTO.setPassword("testUserPassword12");

        //유저 등록
        mockMvc.perform(post(saveUserURL).content(mapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        //데이터 베이스 확인
        user = userRepository.findByIdx(2L);
        assertThat(user).isNull();

        //이메일 형식이 틀렸을 경우
        userDTO.setPassword("testPassword");
        userDTO.setEmail("test");

        //유저 등록
        mockMvc.perform(post(saveUserURL).content(mapper.writeValueAsString(userDTO)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        //데이터 베이스 확인
        user = userRepository.findByIdx(2L);
        assertThat(user).isNull();

        //URL 형식이 틀렸을 경우
        userDTO.setEmail("test@gmail.com");
        userDTO.setGitAddr("address");

        //유저 등록
        mockMvc.perform(post(saveUserURL).content(mapper.writeValueAsString(userDTO)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        //데이터 베이스 확인
        user = userRepository.findByIdx(2L);
        assertThat(user).isNull();

    }

    @Test
    public void 유저아이디_중복_테스트() throws Exception {

        //유저 생성
        UserDto userDto = UserDto.builder()
                            .username("testId")
                            .password("testPassword")
                            .email("test@gmail.com")
                            .build();

        //유저 등록, 성공 확인
        mockMvc.perform(post("/user")
                .content(mapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());

        //같은 아이디의 유저 등록, 실패 확인
        mockMvc.perform(post("/user")
                .content(mapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //다른 아이디의 유저 생성
        userDto = UserDto.builder().username("testId2").password("testPassword").email("test@gmail.com").build();

        //유저 등록, 성공 확인
        mockMvc.perform(post("/user")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isCreated());
    }

    @Test
    public void 유효성검사_실패시_반환메시지_확인() throws Exception {

        //아이디가 유효성검사에 실패하는 경우
        UserDto userDto = new UserDto();
        userDto.setUsername("tes");
        userDto.setPassword("testPassword");
        userDto.setEmail("test@gmail.com");

        mockMvc.perform(post("/user")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isBadRequest());

        //비밀번호가 유효성검사에 실패하는 경우
        userDto.setUsername("testId");
        userDto.setPassword("test");

        mockMvc.perform(post("/user")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isBadRequest());

        //이메일이 유효성검사에 실패하는 경우
        userDto.setPassword("testPassword");
        userDto.setEmail("test.com");
        mockMvc.perform(post("/user")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
    }

}
