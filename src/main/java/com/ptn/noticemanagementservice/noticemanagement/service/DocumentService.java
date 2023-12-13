package com.ptn.noticemanagementservice.noticemanagement.service;

import com.ptn.noticemanagementservice.common.exception.ResourceNotFoundException;
import com.ptn.noticemanagementservice.noticemanagement.entity.Document;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface DocumentService {

    ResponseEntity<Resource> getDocumentResource(Long documentId) throws ResourceNotFoundException;

    Document findById(Long documentId) throws ResourceNotFoundException;
}
