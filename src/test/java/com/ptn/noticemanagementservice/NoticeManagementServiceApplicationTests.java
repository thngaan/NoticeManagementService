package com.ptn.noticemanagementservice;

import com.ptn.noticemanagementservice.config.SpringSecurityWebAuxTestConfig;
import com.ptn.noticemanagementservice.usermanagement.request.RegistrationRequest;
import com.ptn.noticemanagementservice.usermanagement.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {NoticeManagementServiceApplication.class, SpringSecurityWebAuxTestConfig.class}
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:/application-test.properties")
class NoticeManagementServiceApplicationTests {

    @Autowired
    private AccountService accountService;

    @Test
    void contextLoads() {
        initUser();
    }

    public void initUser() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setFullName("full name");
        registrationRequest.setUsername("username1");
        registrationRequest.setPassword("1234");
        registrationRequest.setEmail("email@ptn.com");
        registrationRequest.setPhoneNumber("1234567890");

        RegistrationRequest registrationRequest2 = new RegistrationRequest();
        registrationRequest2.setFullName("full name 2");
        registrationRequest2.setUsername("username2");
        registrationRequest2.setPassword("123456789");
        registrationRequest2.setEmail("email2@ptn.com");
        registrationRequest2.setPhoneNumber("1234567892");

        accountService.register(registrationRequest);
        accountService.register(registrationRequest2);
    }

}
