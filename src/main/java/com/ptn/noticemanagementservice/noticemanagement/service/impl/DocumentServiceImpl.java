package com.ptn.noticemanagementservice.noticemanagement.service.impl;

import com.ptn.noticemanagementservice.common.exception.ResourceNotFoundException;
import com.ptn.noticemanagementservice.common.utils.ValidationUtils;
import com.ptn.noticemanagementservice.noticemanagement.entity.Document;
import com.ptn.noticemanagementservice.noticemanagement.entity.Notice;
import com.ptn.noticemanagementservice.noticemanagement.repository.DocumentRepository;
import com.ptn.noticemanagementservice.noticemanagement.service.DocumentService;
import com.ptn.noticemanagementservice.usermanagement.service.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, AuthenticationFacade authenticationFacade) {
        this.documentRepository = documentRepository;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public Document findById(Long documentId) throws ResourceNotFoundException {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> {
                    LOGGER.error("Document id {} is not found", documentId);
                    return new ResourceNotFoundException(Document.class, documentId);
                });
    }

    @Override
    public ResponseEntity<Resource> getDocumentResource(Long documentId) throws ResourceNotFoundException {
        Document document = findById(documentId);
        Notice notice = document.getNotice();
        if (notice.isDeleted()) {
            LOGGER.error("Notice id {} is deleted", notice.getId());
            throw new ResourceNotFoundException(Notice.class, notice.getId());
        }

        if (!authenticationFacade.isSameAccount(document.getNotice().getAccount())) {
            ValidationUtils.validateActiveTime(notice.getStartDate(), notice.getEndDate());
        }

        String fileName = document.getFileName();
        byte[] file = document.getFileContent();
        ByteArrayResource resource = new ByteArrayResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.valueOf(document.getContentType()))
                .body(resource);
    }
}
