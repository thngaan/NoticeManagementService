package com.ptn.noticemanagementservice.noticemanagement.controller;

import com.ptn.noticemanagementservice.common.builder.GenericResponseBuilder;
import com.ptn.noticemanagementservice.common.dto.ErrorDto;
import com.ptn.noticemanagementservice.common.exception.ResourceNotFoundException;
import com.ptn.noticemanagementservice.common.response.GenericResponse;
import com.ptn.noticemanagementservice.noticemanagement.dto.NoticeDto;
import com.ptn.noticemanagementservice.noticemanagement.request.NoticeRequest;
import com.ptn.noticemanagementservice.noticemanagement.service.NoticeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;

import static com.ptn.noticemanagementservice.common.contant.RestURI.ID;
import static com.ptn.noticemanagementservice.common.contant.RestURI.NOTICE_API;

@RestController
@RequestMapping(NOTICE_API)
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    /**
     * Get Notice by Id API
     *
     * @param id is notice's id
     * @return GenericResponse<NoticeDto>
     * @throws ResourceNotFoundException when not found notice
     */
    @GetMapping(ID)
    public ResponseEntity<GenericResponse<NoticeDto>> get(@PathVariable Long id) throws ResourceNotFoundException {
        NoticeDto noticeDto = noticeService.get(id);

        GenericResponse<NoticeDto> response = new GenericResponseBuilder<NoticeDto>()
                .setMessage(String.format("Success to get notice id = %s", id))
                .setData(noticeDto)
                .buildSuccess();
        return ResponseEntity.ok(response);
    }

    /**
     * Create Notice API
     *
     * @param notice      object notice from user
     * @param attachments all attachment from user
     * @return NoticeDto in DB
     * @throws ResourceNotFoundException if user is not found
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GenericResponse<NoticeDto>> create(@Validated @RequestPart NoticeRequest notice,
                                                             @RequestPart(required = false) List<MultipartFile> attachments) throws ResourceNotFoundException {
        if (!CollectionUtils.isEmpty(notice.getDocuments()) && !CollectionUtils.isEmpty(attachments) && (notice.getDocuments().size() < attachments.size())) {
            GenericResponse<NoticeDto> response = new GenericResponseBuilder<>()
                    .setMessage("Failure to create notice")
                    .setErrors(Arrays.asList(new ErrorDto("notice.documents", "size is invalid")))
                    .buildFailure();
            return ResponseEntity.badRequest().body(response);
        }

        if (notice.getStartDate().after(notice.getEndDate())) {
            GenericResponse<NoticeDto> response = new GenericResponseBuilder<>()
                    .setMessage("Failure to create notice")
                    .setErrors(Arrays.asList(new ErrorDto("notice.startDate", "should before notice.endDate")))
                    .buildFailure();
            return ResponseEntity.badRequest().body(response);
        }


        NoticeDto noticeDto = noticeService.create(notice, attachments);

        GenericResponse<NoticeDto> response = new GenericResponseBuilder<>()
                .setMessage("Success to create notice")
                .setData(noticeDto)
                .buildSuccess();
        return ResponseEntity.ok(response);
    }

    /**
     * Update Notice API
     *
     * @param id          notice's id
     * @param notice      notice content that user want to update
     * @param attachments attachment that user want to add/update
     * @return NoticeDto from DB
     * @throws ResourceNotFoundException if notice or user is not found
     */
    @PutMapping(value = ID, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GenericResponse<NoticeDto>> update(@PathVariable Long id, @Validated @RequestPart NoticeRequest notice,
                                                             @RequestPart(required = false) List<MultipartFile> attachments) throws ResourceNotFoundException, AccessDeniedException {
        if (!CollectionUtils.isEmpty(notice.getDocuments()) && !CollectionUtils.isEmpty(attachments) && (notice.getDocuments().size() < attachments.size())) {
            GenericResponse<NoticeDto> response = new GenericResponseBuilder<>()
                    .setMessage("Failure to update notice")
                    .setErrors(Arrays.asList(new ErrorDto("notice.documents", "size is invalid")))
                    .buildFailure();
            return ResponseEntity.badRequest().body(response);
        }

        if (notice.getStartDate().after(notice.getEndDate())) {
            GenericResponse<NoticeDto> response = new GenericResponseBuilder<>()
                    .setMessage("Failure to update notice")
                    .setErrors(Arrays.asList(new ErrorDto("notice.startDate", "should before notice.endDate")))
                    .buildFailure();
            return ResponseEntity.badRequest().body(response);
        }

        notice.setId(id);
        NoticeDto noticeDto = noticeService.update(notice, attachments);

        GenericResponse<NoticeDto> response = new GenericResponseBuilder<>()
                .setMessage(String.format("Success to update notice id = %s", id))
                .setData(noticeDto)
                .buildSuccess();
        return ResponseEntity.ok(response);
    }

    /**
     * Delete Notice by Id
     *
     * @param id notice's id
     * @return message success
     * @throws ResourceNotFoundException if notice or user is not found
     */
    @DeleteMapping(ID)
    public ResponseEntity<GenericResponse> delete(@PathVariable Long id) throws ResourceNotFoundException, AccessDeniedException {
        noticeService.softDelete(id);

        GenericResponse response = new GenericResponseBuilder<>()
                .setMessage(String.format("Success to delete notice id = %s", id))
                .buildSuccess();
        return ResponseEntity.ok(response);
    }
}
