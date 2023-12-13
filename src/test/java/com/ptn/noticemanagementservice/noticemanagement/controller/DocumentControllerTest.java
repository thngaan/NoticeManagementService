package com.ptn.noticemanagementservice.noticemanagement.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptn.noticemanagementservice.NoticeManagementServiceApplication;
import com.ptn.noticemanagementservice.common.response.GenericResponse;
import com.ptn.noticemanagementservice.config.SpringSecurityWebAuxTestConfig;
import com.ptn.noticemanagementservice.noticemanagement.dto.NoticeDto;
import com.ptn.noticemanagementservice.noticemanagement.request.DocumentRequest;
import com.ptn.noticemanagementservice.noticemanagement.request.NoticeRequest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.ptn.noticemanagementservice.common.contant.RestURI.DOCUMENT_API;
import static com.ptn.noticemanagementservice.common.contant.RestURI.NOTICE_API;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {NoticeManagementServiceApplication.class, SpringSecurityWebAuxTestConfig.class}
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:/application-test.properties")
@FixMethodOrder(NAME_ASCENDING)
public class DocumentControllerTest {

    private static Long id;
    private static Long documentId;
    private static Long documentId2;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("username1")
    public void order1_shouldInsertNotice1_NotActiveDate() throws Exception {
        NoticeRequest noticeRequest = new NoticeRequest();
        noticeRequest.setContentDetail("Hi there");
        noticeRequest.setTitle("New title");
        noticeRequest.setStartDate(new Date());
        noticeRequest.setEndDate(new Date());

        DocumentRequest documentRequest1 = new DocumentRequest();
        documentRequest1.setOrder(1);
        documentRequest1.setFilename("file1.txt");

        DocumentRequest documentRequest2 = new DocumentRequest();
        documentRequest2.setOrder(2);
        documentRequest2.setFilename("file2.txt");

        noticeRequest.setDocuments(Arrays.asList(documentRequest1, documentRequest2));

        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(noticeRequest);


        MockMultipartFile firstFile = new MockMultipartFile("attachments", "file1.txt", "text/plain", "File 1".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("attachments", "file2.txt", "text/plain", "File 2".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("notice", "", "application/json", jsonStr.getBytes());

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
        documentId = noticeDto.getData().getDocuments().get(0).getId();
    }

    @Test
    @WithUserDetails("username1")
    public void order1_shouldInsertNotice1_NotActive() throws Exception {
        NoticeRequest noticeRequest = new NoticeRequest();
        noticeRequest.setContentDetail("Hi there");
        noticeRequest.setTitle("New title");

        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 1);

        noticeRequest.setStartDate(currentDate);
        noticeRequest.setEndDate(c.getTime());

        DocumentRequest documentRequest1 = new DocumentRequest();
        documentRequest1.setOrder(1);
        documentRequest1.setFilename("file1.txt");

        DocumentRequest documentRequest2 = new DocumentRequest();
        documentRequest2.setOrder(2);
        documentRequest2.setFilename("file2.txt");

        noticeRequest.setDocuments(Arrays.asList(documentRequest1, documentRequest2));

        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(noticeRequest);


        MockMultipartFile firstFile = new MockMultipartFile("attachments", "file1.txt", "text/plain", "File 1".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("attachments", "file2.txt", "text/plain", "File 2".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("notice", "", "application/json", jsonStr.getBytes());

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
        documentId2 = noticeDto.getData().getDocuments().get(0).getId();
    }

    @Test
    @WithUserDetails("username1")
    public void order2_shouldGetDocSuccess_noticeNotActive_Author() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(DOCUMENT_API + "/" + documentId))
                .andExpect(status().is(200))
                .andReturn();
    }

    @Test
    @WithUserDetails("username2")
    public void order2_shouldGetDocFail_noticeNotActive_NotAuthor() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(DOCUMENT_API + "/" + documentId))
                .andExpect(status().is(400))
                .andReturn();
    }

    @Test
    @WithUserDetails("username2")
    public void order2_shouldGetDocSuccess_noticeActive_NotAuthor() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(DOCUMENT_API + "/" + documentId2))
                .andExpect(status().is(200))
                .andReturn();
    }

    @Test
    @WithUserDetails("username1")
    public void order3_shouldGetDocFail_NoticeIsDeleted() throws Exception {
        // delete notice
        this.mockMvc
                .perform(delete(NOTICE_API + "/" + id))
                .andExpect(status().is(200));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get(DOCUMENT_API + "/" + documentId))
                .andExpect(status().is(400))
                .andReturn();
    }

    @Test
    @WithUserDetails("username1")
    public void order3_shouldGetDocFail_DocumentNotExisted() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(DOCUMENT_API + "/" + 20))
                .andExpect(status().is(400))
                .andReturn();
    }
}
