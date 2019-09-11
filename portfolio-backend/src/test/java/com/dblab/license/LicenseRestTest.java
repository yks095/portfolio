package com.dblab.license;

import com.dblab.domain.License;
import com.dblab.domain.User;
import com.dblab.dto.LicenseDto;
import com.dblab.dto.UserDto;
import com.dblab.repository.LicenseRepository;
import com.dblab.repository.UserRepository;
import com.dblab.service.CustomUserDetailsService;
import com.dblab.service.LicenseService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LicenseRestTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private LicenseRepository licenseRepository;

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LicenseService licenseService;

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
            LicenseDto licenseDto= new LicenseDto();
            licenseDto.setName("name" + i);
            licenseDto.setPeriod("period" + i);
            licenseDto.setInstitution("institution" + i);
            licenseService.saveLicense(licenseDto, user);
        }

        assertThat(user.getLicenses().size()).isEqualTo(10);

        mockMvc.perform(get("/api/licenses")
                .with(csrf())
                .with(user(userDetails)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void saveLicenseTest() throws Exception  {

        mockMvc.perform(get("/api/licenses")
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        LicenseDto licenseDto = new LicenseDto();
        licenseDto.setName("name_test");
        licenseDto.setPeriod("period_test");
        licenseDto.setInstitution("institution_test");

        // post
        mockMvc.perform(post("/api/licenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(licenseDto))
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isCreated());

        // 이름, 기간, 발급기관 확인
        License license = licenseRepository.findByIdx(1L);
        assertThat(license).isNotNull();
        assertThat(license.getName()).isEqualTo("name_test");
        assertThat(license.getPeriod()).isEqualTo("period_test");
        assertThat(license.getInstitution()).isEqualTo("institution_test");


    }

    @Test
    public void modifyLicenseTest() throws Exception  {

        mockMvc.perform(get("/api/licenses")
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        LicenseDto modifiedLicenseDto = new LicenseDto();
        modifiedLicenseDto.setName("name_test");
        modifiedLicenseDto.setPeriod("period_test");
        modifiedLicenseDto.setInstitution("institution_test");

        // post
        mockMvc.perform(post("/api/licenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifiedLicenseDto))
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isCreated());

        // 이름, 기간, 발급기관 확인
        License modifiedLicense = licenseRepository.findByIdx(1L);
        assertThat(modifiedLicense).isNotNull();
        assertThat(modifiedLicense.getName()).isEqualTo("name_test");
        assertThat(modifiedLicense.getPeriod()).isEqualTo("period_test");
        assertThat(modifiedLicense.getInstitution()).isEqualTo("institution_test");

        // 이름, 기간, 발급기관 수정
        modifiedLicenseDto.setName("modified_name_test");
        modifiedLicenseDto.setPeriod("modified_period_test");
        modifiedLicenseDto.setInstitution("modified_institution_test");

        // put
        mockMvc.perform(put("/api/licenses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifiedLicenseDto))
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        // 수정된 이름, 기간, 발급기관 확인
        modifiedLicense = licenseRepository.findByIdx(1L);
        assertThat(modifiedLicense).isNotNull();
        assertThat(modifiedLicense.getName()).isEqualTo("modified_name_test");
        assertThat(modifiedLicense.getPeriod()).isEqualTo("modified_period_test");
        assertThat(modifiedLicense.getInstitution()).isEqualTo("modified_institution_test");
    }

    @Test
    public void deleteLicenseTest() throws Exception  {

        mockMvc.perform(get("/api/licenses")
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        LicenseDto deletedLicenseDto = new LicenseDto();
        deletedLicenseDto.setName("name_test");
        deletedLicenseDto.setPeriod("period_test");
        deletedLicenseDto.setInstitution("institution_test");

        // post
        mockMvc.perform(post("/api/licenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deletedLicenseDto))
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isCreated());

        // 이름, 기간, 인원, 발급기관 확인
        License deletedLicense = licenseRepository.findByIdx(1L);
        assertThat(deletedLicense).isNotNull();
        assertThat(deletedLicense.getName()).isEqualTo("name_test");
        assertThat(deletedLicense.getPeriod()).isEqualTo("period_test");
        assertThat(deletedLicense.getInstitution()).isEqualTo("institution_test");

        // delete
        mockMvc.perform(delete("/api/licenses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deletedLicense))
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());

        // 삭제된 프로젝트 확인
        deletedLicense = licenseRepository.findByIdx(1L);
        assertThat(deletedLicense).isNull();
    }

}
