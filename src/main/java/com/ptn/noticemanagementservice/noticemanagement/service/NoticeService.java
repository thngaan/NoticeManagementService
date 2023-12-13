package com.ptn.noticemanagementservice.noticemanagement.service;

import com.ptn.noticemanagementservice.common.exception.ResourceNotFoundException;
import com.ptn.noticemanagementservice.noticemanagement.dto.NoticeDto;
import com.ptn.noticemanagementservice.noticemanagement.request.NoticeRequest;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface NoticeService {

    NoticeDto get(Long id) throws ResourceNotFoundException;

    NoticeDto create(NoticeRequest noticeRequest, List<MultipartFile> attachments) throws ResourceNotFoundException;

    NoticeDto update(NoticeRequest noticeRequest, List<MultipartFile> attachments) throws ResourceNotFoundException, AccessDeniedException;

    void softDelete(Long Id) throws ResourceNotFoundException, AccessDeniedException;

    void hardDelete(Long Id);
}
