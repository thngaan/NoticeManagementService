package com.ptn.noticemanagementservice.noticemanagement.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptn.noticemanagementservice.NoticeManagementServiceApplication;
import com.ptn.noticemanagementservice.common.response.GenericResponse;
import com.ptn.noticemanagementservice.config.SpringSecurityWebAuxTestConfig;
import com.ptn.noticemanagementservice.noticemanagement.dto.NoticeDto;
import com.ptn.noticemanagementservice.noticemanagement.request.DocumentRequest;
import com.ptn.noticemanagementservice.noticemanagement.request.NoticeRequest;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.ptn.noticemanagementservice.common.contant.RestURI.NOTICE_API;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {NoticeManagementServiceApplication.class, SpringSecurityWebAuxTestConfig.class}
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:/application-test.properties")
@FixMethodOrder(NAME_ASCENDING)
public class NoticeControllerTest {

    private static Long id;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("username1")
    public void order1_shouldInsertFail_NoticeInvalid() throws Exception {
        NoticeRequest noticeRequest = new NoticeRequest();

        DocumentRequest documentRequest1 = new DocumentRequest();
        documentRequest1.setOrder(1);
        documentRequest1.setFileName("file1.txt");

        DocumentRequest documentRequest2 = new DocumentRequest();
        documentRequest2.setOrder(2);
        documentRequest2.setFileName("file2.txt");

        noticeRequest.setDocuments(Arrays.asList(documentRequest1, documentRequest2));

        ObjectMapper mapper = new ObjectMapper();
        String json_string = mapper.writeValueAsString(noticeRequest);


        MockMultipartFile firstFile = new MockMultipartFile("attachments", "file1.txt", "text/plain", "File 1".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("attachments", "file2.txt", "text/plain", "File 2".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("notice", "", "application/json", json_string.getBytes());

        this.mockMvc
                .perform(multipart(NOTICE_API)
                        .file(firstFile)
                        .file(secondFile)
                        .file(jsonFile))
                .andExpect(status().is(400));
    }

    @Test
    @WithUserDetails("username1")
    public void order1_shouldInsertSuccess() throws Exception {
        NoticeRequest noticeRequest = new NoticeRequest();
        noticeRequest.setContentDetail("Hi there");
        noticeRequest.setTitle("New title");
        noticeRequest.setStartDate(new Date());
        noticeRequest.setEndDate(new Date());

        DocumentRequest documentRequest1 = new DocumentRequest();
        documentRequest1.setOrder(1);
        documentRequest1.setFileName("file1.txt");

        DocumentRequest documentRequest2 = new DocumentRequest();
        documentRequest2.setOrder(2);
        documentRequest2.setFileName("file2.txt");

        noticeRequest.setDocuments(Arrays.asList(documentRequest1, documentRequest2));

        ObjectMapper mapper = new ObjectMapper();
        String json_string = mapper.writeValueAsString(noticeRequest);


        MockMultipartFile firstFile = new MockMultipartFile("attachments", "file1.txt", "text/plain", "File 1".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("attachments", "file2.txt", "text/plain", "File 2".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("notice", "", "application/json", json_string.getBytes());

        MvcResult result = this.mockMvc
                .perform(multipart(NOTICE_API)
                        .file(firstFile)
                        .file(secondFile)
                        .file(jsonFile))
                .andExpect(status().is(200))
                .andReturn();

        TypeReference<GenericResponse<NoticeDto>> typeRef = new TypeReference<GenericResponse<NoticeDto>>() {
        };
        GenericResponse<NoticeDto> noticeDto = mapper.readValue(result.getResponse().getContentAsString(), typeRef);
        id = noticeDto.getData().getId();
    }

    @Test
    @WithUserDetails("username1")
    public void order2_shouldGetSuccess_NoticeNotActive_Author() throws Exception {
        MvcResult result = this.mockMvc
                .perform(get(NOTICE_API + "/" + id))
                .andExpect(status().is(200))
                .andReturn();
        TypeReference<GenericResponse<NoticeDto>> typeRef = new TypeReference<GenericResponse<NoticeDto>>() {
        };
        ObjectMapper mapper = new ObjectMapper();
        GenericResponse<NoticeDto> noticeDto = mapper.readValue(result.getResponse().getContentAsString(), typeRef);
        Assert.assertEquals(0, noticeDto.getData().getNumberOfViews().intValue());
    }

    @Test
    @WithUserDetails("username2")
    public void order2_shouldGetFail_NoticeNotActive_NotAuthor() throws Exception {
        this.mockMvc
                .perform(get(NOTICE_API + "/" + id))
                .andExpect(status().is(400));
    }

    @Test
    @WithUserDetails("username1")
    public void order3_shouldUpdateSuccess_notDelete_author() throws Exception {
        NoticeRequest noticeRequest = new NoticeRequest();
        noticeRequest.setId(id);
        noticeRequest.setContentDetail("Hi there 2");
        noticeRequest.setTitle("New title 2");

        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 1);

        noticeRequest.setStartDate(currentDate);
        noticeRequest.setEndDate(c.getTime());

        DocumentRequest documentRequest1 = new DocumentRequest();
        documentRequest1.setOrder(1);
        documentRequest1.setFileName("file3.txt");

        noticeRequest.setDocuments(Arrays.asList(documentRequest1));

        ObjectMapper mapper = new ObjectMapper();
        String json_string = mapper.writeValueAsString(noticeRequest);


        MockMultipartFile firstFile = new MockMultipartFile("attachments", "file3.txt", "text/plain", "File 3".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("notice", "", "application/json", json_string.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(NOTICE_API + "/" + id);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        MvcResult result = this.mockMvc.perform(builder
                        .file(firstFile)
                        .file(jsonFile))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<GenericResponse<NoticeDto>> typeRef = new TypeReference<>() {
        };
        GenericResponse<NoticeDto> noticeDto = mapper.readValue(result.getResponse().getContentAsString(), typeRef);
        id = noticeDto.getData().getId();

        Assert.assertTrue(noticeDto.getData().getId().compareTo(id) == 0);
        Assert.assertEquals(1, noticeDto.getData().getDocuments().size());
        Assert.assertEquals("New title 2", noticeDto.getData().getTitle());
    }

    @Test
    @WithUserDetails("username2")
    public void order3_shouldUpdateFail_NotAuthor() throws Exception {
        NoticeRequest noticeRequest = new NoticeRequest();
        noticeRequest.setId(id);
        noticeRequest.setContentDetail("Hi there 2");
        noticeRequest.setTitle("New title 2");

        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 1);

        noticeRequest.setStartDate(currentDate);
        noticeRequest.setEndDate(c.getTime());

        DocumentRequest documentRequest1 = new DocumentRequest();
        documentRequest1.setOrder(1);
        documentRequest1.setFileName("file3.txt");

        noticeRequest.setDocuments(Arrays.asList(documentRequest1));

        ObjectMapper mapper = new ObjectMapper();
        String json_string = mapper.writeValueAsString(noticeRequest);


        MockMultipartFile firstFile = new MockMultipartFile("attachments", "file3.txt", "text/plain", "File 3".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("notice", "", "application/json", json_string.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(NOTICE_API + "/" + id);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        this.mockMvc.perform(builder
                        .file(firstFile)
                        .file(jsonFile))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("username1")
    public void order3_shouldUpdateFail_noticeNotExisted() throws Exception {
        NoticeRequest noticeRequest = new NoticeRequest();
        noticeRequest.setId(id);
        noticeRequest.setContentDetail("Hi there 2");
        noticeRequest.setTitle("New title 2");

        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 1);

        noticeRequest.setStartDate(currentDate);
        noticeRequest.setEndDate(c.getTime());

        DocumentRequest documentRequest1 = new DocumentRequest();
        documentRequest1.setOrder(1);
        documentRequest1.setFileName("file3.txt");

        noticeRequest.setDocuments(Arrays.asList(documentRequest1));

        ObjectMapper mapper = new ObjectMapper();
        String json_string = mapper.writeValueAsString(noticeRequest);


        MockMultipartFile firstFile = new MockMultipartFile("attachments", "file3.txt", "text/plain", "File 3".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("notice", "", "application/json", json_string.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(NOTICE_API + "/" + 20);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        this.mockMvc.perform(builder
                        .file(firstFile)
                        .file(jsonFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("username2")
    public void order4_shouldGetAndIncreaseViewCounter_NotAuthor() throws Exception {
        MvcResult result = this.mockMvc
                .perform(get(NOTICE_API + "/" + id))
                .andExpect(status().is(200))
                .andReturn();

        TypeReference<GenericResponse<NoticeDto>> typeRef = new TypeReference<GenericResponse<NoticeDto>>() {
        };
        ObjectMapper mapper = new ObjectMapper();
        GenericResponse<NoticeDto> noticeDto = mapper.readValue(result.getResponse().getContentAsString(), typeRef);
        Assert.assertEquals(1, noticeDto.getData().getNumberOfViews().intValue());
    }

    @Test
    @WithUserDetails("username1")
    public void order5_shouldDeleteSuccess_Author() throws Exception {
        this.mockMvc
                .perform(delete(NOTICE_API + "/" + id))
                .andExpect(status().is(200));
    }

    @Test
    @WithUserDetails("username2")
    public void order5_shouldDeleteFail_NotAuthor() throws Exception {
        this.mockMvc
                .perform(delete(NOTICE_API + "/" + id))
                .andExpect(status().is(403));
    }

    @Test
    @WithUserDetails("username1")
    public void order5_shouldGetFail_NoticeIsDeleted_Author() throws Exception {
        this.mockMvc
                .perform(get(NOTICE_API + "/" + id))
                .andExpect(status().is(400));
    }
}