package com.dblab.login;

import com.dblab.config.jwtConfig.JwtTokenUtil;
import com.dblab.domain.User;
import com.dblab.dto.UserDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void loginTest() throws Exception {

        //회원 가입 전 로그인
        mockMvc.perform(formLogin().user("testUserName").password("testUserPassword"))
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated())
                .andExpect(status().is3xxRedirection());

        //유저 생성
        UserDto userDTO = new UserDto();
        userDTO.setUsername("testUserName");
        userDTO.setPassword("testUserPassword");
        userDTO.setEmail("test@gmail.com");

        //회원 가입
        mockMvc.perform(post("/user").content(objectMapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf()))
                .andExpect(status().isCreated());

        //데이터 베이스 확인
        User user = userRepository.findByIdx(1L);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testUserName");
        assertThat(user.getEmail()).isEqualTo("test@gmail.com");

        //아이디, 패스워드가 모두 맞게 로그인 하였을 경우
        mockMvc.perform(formLogin().user("testUserName").password("testUserPassword"))
                .andExpect(forwardedUrl("/login/success"))
                .andExpect(authenticated())
                .andExpect(status().is2xxSuccessful());

        //아이디를 틀릴 경우
        mockMvc.perform(formLogin().user("ihaventid").password("testUserPassword"))
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated()).andExpect(status().is3xxRedirection());

        //비밀번호를 틀릴 경우
        mockMvc.perform(formLogin().user("testUserPassword").password("ihaventpassword"))
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated()).andExpect(status().is3xxRedirection());
    }

    @Test
    public void 로그아웃_테스트() throws Exception{

        //유저 생성
        UserDto userDto = createUserDto(1);

        //유저 등록
        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());

        User user = userRepository.findByUsername("testId1");

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testId1");
        assertThat(user.getEmail()).isEqualTo("test@gmail.com");


        //로그인 --> 토큰 생성 반환 확인
        mockMvc.perform(post("/login/authenticate")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        //데이터베이스에 없는 사용자 --> 로그인 실패
        UserDto userDto2 = createUserDto(2);

        mockMvc.perform(post("/login/authenticate")
                .content(objectMapper.writeValueAsString(userDto2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        //토큰 생성
        String jwtToken = "Bearer " + jwtTokenUtil.generateToken(user);

        //인증이 필요한 페이지 요청 --> 성공
        mockMvc.perform(get("/api/projects")
                        .header("Authorization", jwtToken))
                        .andExpect(status().isOk());

        //로그아웃
        mockMvc.perform(get("/logouting")
                        .header("Authorization", jwtToken))
                        .andExpect(status().isOk())
                        .andDo(print());

        //인증이 필요한 페이지 요청 --> 실패
        mockMvc.perform(get("/api/projects")
                        .header("Authorization", jwtToken))
                        .andExpect(status().is4xxClientError());

    }

    //UserDto 객체를 생성하는 메소드
    public UserDto createUserDto(int idx){
        return UserDto.builder().username("testId" + idx).password("testPassword").email("test@gmail.com").build();
    }
}