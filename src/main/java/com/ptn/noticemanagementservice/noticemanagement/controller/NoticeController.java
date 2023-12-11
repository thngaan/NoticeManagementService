package com.ptn.noticemanagementservice.noticemanagement.controller;

import com.ptn.noticemanagementservice.noticemanagement.dto.NoticeDto;
import com.ptn.noticemanagementservice.noticemanagement.request.NoticeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    @GetMapping("/{id}")
    public NoticeDto get(@PathVariable Long id) {
        return null;
    }

    @PostMapping(value = "/upload/multiple/files2", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> uploadMultipleFiles(@RequestParam("multipleFiles") List<MultipartFile> files)
            throws Exception {

        if (files == null || files.isEmpty()) {
            throw new RuntimeException("You must select at least one file for uploading");
        }

        StringBuilder sb = new StringBuilder(files.size());

        for (int i = 0; i < files.size(); i++) {
            InputStream inputStream = files.get(i).getInputStream();
            String originalName = files.get(i).getOriginalFilename();
            String name = files.get(i).getName();
            String contentType = files.get(i).getContentType();

            sb.append("File Name: " + originalName + "\n");
        }

        // Do processing with uploaded file data in Service layer
        return new ResponseEntity<String>(sb.toString(), HttpStatus.OK);
    }

    @PostMapping
    public NoticeDto create(@RequestBody NoticeRequest noticeRequest) {
        return null;
    }

    @PutMapping("/{id}")
    public NoticeDto update(@PathVariable Long id, @RequestBody NoticeRequest noticeRequest) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

    }
}
