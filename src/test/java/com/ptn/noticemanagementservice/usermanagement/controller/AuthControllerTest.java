package com.ptn.noticemanagementservice.usermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptn.noticemanagementservice.NoticeManagementServiceApplication;
import com.ptn.noticemanagementservice.config.SpringSecurityWebAuxTestConfig;
import com.ptn.noticemanagementservice.usermanagement.request.LoginRequest;
import com.ptn.noticemanagementservice.usermanagement.request.RegistrationRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.ptn.noticemanagementservice.common.contant.RestURI.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {NoticeManagementServiceApplication.class, SpringSecurityWebAuxTestConfig.class}
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:/application-test.properties")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateUser() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setFullName("full name 2");
        registrationRequest.setUsername("username3");
        registrationRequest.setPassword("1234");
        registrationRequest.setEmail("email3@ptn.com");
        registrationRequest.setPhoneNumber("1234567895");


        ObjectMapper mapper = new ObjectMapper();
        String jsonStr1 = mapper.writeValueAsString(registrationRequest);

        this.mockMvc
                .perform(post(API + REGISTRATION).contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonStr1))
                .andExpect(status().is(200));
    }

    @Test
    public void shouldCreateUserFail() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setFullName("full name");
        registrationRequest.setUsername("username2");
        registrationRequest.setPassword("1234");
        registrationRequest.setEmail("email3@ptn.com");
        registrationRequest.setPhoneNumber("1234567893");


        ObjectMapper mapper = new ObjectMapper();
        String jsonStr1 = mapper.writeValueAsString(registrationRequest);

        this.mockMvc
                .perform(post(API + REGISTRATION).contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonStr1))
                .andExpect(status().is(400));
    }

    @Test
    public void shouldLoginSuccess() throws Exception {

        LoginRequest loginRequest2 = new LoginRequest();
        loginRequest2.setUsername("username2");
        loginRequest2.setPassword("123456789");

        ObjectMapper mapper = new ObjectMapper();
        String jsonStr2 = mapper.writeValueAsString(loginRequest2);

        this.mockMvc
                .perform(post(API + LOGIN).contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonStr2))
                .andExpect(status().is(200));
    }

    @Test
    public void shouldLoginFail() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username12");
        loginRequest.setPassword("abcd");

        ObjectMapper mapper = new ObjectMapper();
        String jsonStr1 = mapper.writeValueAsString(loginRequest);

        this.mockMvc
                .perform(post(API + LOGIN).contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonStr1))
                .andExpect(status().is(401));
    }
}
