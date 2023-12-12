package com.ptn.noticemanagementservice.noticemanagement.controller;

import com.ptn.noticemanagementservice.common.builder.GenericResponseBuilder;
import com.ptn.noticemanagementservice.common.exception.ResourceNotFoundException;
import com.ptn.noticemanagementservice.common.response.GenericResponse;
import com.ptn.noticemanagementservice.noticemanagement.dto.NoticeDto;
import com.ptn.noticemanagementservice.noticemanagement.request.NoticeRequest;
import com.ptn.noticemanagementservice.noticemanagement.service.NoticeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    private NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<NoticeDto>> get(@PathVariable Long id) throws ResourceNotFoundException {
        NoticeDto noticeDto = noticeService.get(id);

        GenericResponse<NoticeDto> response = new GenericResponseBuilder<NoticeDto>()
                .setMessage(String.format("Success to get notice id = %s", id))
                .setData(noticeDto)
                .buildSuccess();
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GenericResponse<NoticeDto>> create(@Validated @RequestPart NoticeRequest notice,
                            @RequestPart(required = false) List<MultipartFile> attachments) throws ResourceNotFoundException {
        NoticeDto noticeDto =  noticeService.createUpdate(notice, attachments);

        GenericResponse<NoticeDto> response = new GenericResponseBuilder<>()
                .setMessage("Success to create notice")
                .setData(noticeDto)
                .buildSuccess();
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GenericResponse<NoticeDto>> update(@PathVariable Long id, @Validated @RequestPart NoticeRequest notice,
                            @RequestPart(required = false) List<MultipartFile> attachments) throws ResourceNotFoundException {
        notice.setId(id);
        NoticeDto noticeDto =  noticeService.createUpdate(notice, attachments);

        GenericResponse<NoticeDto> response = new GenericResponseBuilder<>()
                .setMessage(String.format("Success to update notice id = %s", id))
                .setData(noticeDto)
                .buildSuccess();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable Long id) throws ResourceNotFoundException {
        noticeService.softDelete(id);

        GenericResponse response = new GenericResponseBuilder<>()
                .setMessage(String.format("Success to delete notice id = %s", id))
                .buildSuccess();
        return ResponseEntity.ok(response);
    }
}
