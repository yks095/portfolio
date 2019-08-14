package com.dblab.project;

import com.dblab.domain.Project;
import com.dblab.dto.UserDto;
import com.dblab.repository.ProjectRepository;
import com.dblab.service.CustomUserDetailsService;
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
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    private MockMvc mockMvc;

    @Autowired
    CustomUserDetailsService userDetailsService;

    private UserDetails userDetails;

    @Before
    public void setUp() throws Exception    {
        // mock 초기화
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        // user 정보
        UserDto userDto = new UserDto();
        userDto.setUsername("username");
        userDto.setPassword("password");
        userDto.setEmail("email@gmail.com");

        // user 생성
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))
                .with(csrf()))
                .andExpect(status().isCreated());

        // 로그인
        mockMvc.perform(formLogin()
                .user("username")
                .password("password"))
                .andExpect(forwardedUrl("/login/success"));

        userDetails = userDetailsService.loadUserByUsername("username");

        mockMvc.perform(get("/main")
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());
    }

    @Test
    public void saveProjectTest() throws Exception  {
        Project project = new Project();
        project.setName("name_test");
        project.setPeriod("period_test");
        project.setPersons("persons_test");
        project.setDescription("description_test");

        // post
        mockMvc.perform(post("/project")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(project))
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isCreated());

        // 이름, 기간, 인원, 내용, 등록날짜 확인
        project = projectRepository.findByIdx(1L);
        assertThat(project).isNotNull();
        assertThat(project.getName()).isEqualTo("name_test");
        assertThat(project.getPeriod()).isEqualTo("period_test");
        assertThat(project.getPersons()).isEqualTo("persons_test");
        assertThat(project.getDescription()).isEqualTo("description_test");


    }

    @Test
    public void modifyProjectTest() throws Exception  {
        Project modifiedProject = new Project();
        modifiedProject.setName("name_test");
        modifiedProject.setPeriod("period_test");
        modifiedProject.setPersons("persons_test");
        modifiedProject.setDescription("description_test");

        // post
        mockMvc.perform(post("/project")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifiedProject))
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isCreated());

        // 이름, 기간, 인원, 내용 확인
        modifiedProject = projectRepository.findByIdx(1L);
        assertThat(modifiedProject).isNotNull();
        assertThat(modifiedProject.getName()).isEqualTo("name_test");
        assertThat(modifiedProject.getPeriod()).isEqualTo("period_test");
        assertThat(modifiedProject.getPersons()).isEqualTo("persons_test");
        assertThat(modifiedProject.getDescription()).isEqualTo("description_test");

        // 이름, 기간, 인원, 내용, 수정
        modifiedProject.setName("modified_name_test");
        modifiedProject.setPeriod("modified_period_test");
        modifiedProject.setPersons("modified_persons_test");
        modifiedProject.setDescription("modified_description_test");

        // put
        mockMvc.perform(put("/project/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifiedProject))
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        // 수정된 이름, 기간, 인원, 내용 확인
        modifiedProject = projectRepository.findByIdx(1L);
        assertThat(modifiedProject).isNotNull();
        assertThat(modifiedProject.getName()).isEqualTo("modified_name_test");
        assertThat(modifiedProject.getPeriod()).isEqualTo("modified_period_test");
        assertThat(modifiedProject.getPersons()).isEqualTo("modified_persons_test");
        assertThat(modifiedProject.getDescription()).isEqualTo("modified_description_test");
    }

    @Test
    public void deleteProjectTest() throws Exception  {
        Project deletedProject = new Project();
        deletedProject.setName("name_test");
        deletedProject.setPeriod("period_test");
        deletedProject.setPersons("persons_test");
        deletedProject.setDescription("description_test");

        // post
        mockMvc.perform(post("/project")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deletedProject))
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isCreated());

        // 이름, 기간, 인원, 내용, 등록날짜 확인
        deletedProject = projectRepository.findByIdx(1L);
        assertThat(deletedProject).isNotNull();
        assertThat(deletedProject.getName()).isEqualTo("name_test");
        assertThat(deletedProject.getPeriod()).isEqualTo("period_test");
        assertThat(deletedProject.getPersons()).isEqualTo("persons_test");
        assertThat(deletedProject.getDescription()).isEqualTo("description_test");

        // delete
        mockMvc.perform(delete("/project/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deletedProject))
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        // 삭제된 프로젝트 확인
        deletedProject = projectRepository.findByIdx(1L);
        assertThat(deletedProject).isNull();
    }
}
