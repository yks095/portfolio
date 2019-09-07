package com.dblab.hireInfo;

import com.dblab.domain.HireInformation;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HireInformationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final String url = "/api/hireInfos";

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void 사람인api_테스트() throws Exception {
        HireInformation hireInformation = HireInformation.builder()
                                                        .keywords("개발자")
                                                        .loc_cd("101000")
                                                        .build();

        mockMvc.perform(post(url).content(objectMapper.writeValueAsString(hireInformation))
                        .contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
