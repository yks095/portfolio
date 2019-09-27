package com.dblab.project;

import com.dblab.domain.Project;
import com.dblab.domain.User;
import com.dblab.dto.ProjectDto;
import com.dblab.dto.UserDto;
import com.dblab.repository.ProjectRepository;
import com.dblab.repository.UserRepository;
import com.dblab.service.CustomUserDetailsService;
import com.dblab.service.ProjectService;
import com.dblab.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectRestTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

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
        userDto.setProfile("user_profileImg");
        userDto.setGitAddr("https://github.com/username");

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
    public void getMappingTest() throws Exception   {

        User user = userRepository.findByIdx(1L);

        for(int i = 0; i < 10; i++) {
            ProjectDto projectDto = new ProjectDto();
            projectDto.setName("name" + i);
            projectDto.setPeriod("period" + i);
            projectDto.setDescription("description" + i);
            projectDto.setImage("image" + i);
            projectDto.setGitAddr("www.github.com/test" + i);
            projectService.saveProject(projectDto, user);
        }

        assertThat(user.getProjects().size()).isEqualTo(10);

        mockMvc.perform(get("/api/projects")
                .with(csrf())
                .with(user(userDetails)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void saveProjectTest() throws Exception  {

        mockMvc.perform(get("/api/projects")
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("name_test");
        projectDto.setPeriod("period_test");
        projectDto.setDescription("description_test");
        projectDto.setImage("image_test");
        projectDto.setGitAddr("www.github.com/test");

        // post
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectDto))
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isCreated());

        // 이름, 기간, 인원, 내용, 등록날짜 확인
        Project project = projectRepository.findByIdx(1L);
        assertThat(project).isNotNull();
        assertThat(project.getName()).isEqualTo("name_test");
        assertThat(project.getPeriod()).isEqualTo("period_test");
        assertThat(project.getDescription()).isEqualTo("description_test");
        assertThat(project.getImage()).isEqualTo("image_test");
        assertThat(project.getGitAddr()).isEqualTo("www.github.com/test");


    }

    @Test
    public void modifyProjectTest() throws Exception  {

        mockMvc.perform(get("/api/projects")
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        ProjectDto modifiedProjectDto = new ProjectDto();
        modifiedProjectDto.setName("name_test");
        modifiedProjectDto.setPeriod("period_test");
        modifiedProjectDto.setDescription("description_test");
        modifiedProjectDto.setImage("image_test");
        modifiedProjectDto.setGitAddr("www.github.com/test");

        // post
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifiedProjectDto))
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isCreated());

        // 이름, 기간, 인원, 내용 확인
        Project modifiedProject = projectRepository.findByIdx(1L);
        assertThat(modifiedProject).isNotNull();
        assertThat(modifiedProject.getName()).isEqualTo("name_test");
        assertThat(modifiedProject.getPeriod()).isEqualTo("period_test");
        assertThat(modifiedProject.getDescription()).isEqualTo("description_test");

        // 이름, 기간, 인원, 내용 수정
        modifiedProjectDto.setName("modified_name_test");
        modifiedProjectDto.setPeriod("modified_period_test");
        modifiedProjectDto.setDescription("modified_description_test");
        modifiedProjectDto.setImage("modified_image_test");
        modifiedProjectDto.setGitAddr("www.github.com/modified_test");

        // put
        mockMvc.perform(put("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifiedProjectDto))
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        // 수정된 이름, 기간, 인원, 내용 확인
        modifiedProject = projectRepository.findByIdx(1L);
        assertThat(modifiedProject).isNotNull();
        assertThat(modifiedProject.getName()).isEqualTo("modified_name_test");
        assertThat(modifiedProject.getPeriod()).isEqualTo("modified_period_test");
        assertThat(modifiedProject.getDescription()).isEqualTo("modified_description_test");
        assertThat(modifiedProject.getImage()).isEqualTo("modified_image_test");
        assertThat(modifiedProject.getGitAddr()).isEqualTo("www.github.com/modified_test");
    }

    @Test
    public void deleteProjectTest() throws Exception  {

        mockMvc.perform(get("/api/projects")
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        ProjectDto deletedProjectDto = new ProjectDto();
        deletedProjectDto.setName("name_test");
        deletedProjectDto.setPeriod("period_test");
        deletedProjectDto.setDescription("description_test");
        deletedProjectDto.setImage("image_test");
        deletedProjectDto.setGitAddr("www.github.com/test");

        // post
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deletedProjectDto))
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isCreated());

        // 이름, 기간, 인원, 내용, 등록날짜 확인
        Project deletedProject = projectRepository.findByIdx(1L);
        assertThat(deletedProject).isNotNull();
        assertThat(deletedProject.getName()).isEqualTo("name_test");
        assertThat(deletedProject.getPeriod()).isEqualTo("period_test");
        assertThat(deletedProject.getDescription()).isEqualTo("description_test");
        assertThat(deletedProject.getImage()).isEqualTo("image_test");
        assertThat(deletedProject.getGitAddr()).isEqualTo("www.github.com/test");

        // delete
        mockMvc.perform(delete("/api/projects/1")
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
