package com.ptn.noticemanagementservice.noticemanagement.controller;

import com.ptn.noticemanagementservice.common.exception.ResourceNotFoundException;
import com.ptn.noticemanagementservice.noticemanagement.service.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ptn.noticemanagementservice.common.contant.RestURI.DOCUMENT_API;
import static com.ptn.noticemanagementservice.common.contant.RestURI.ID;

@RestController
@RequestMapping(DOCUMENT_API)
public class DocumentController {

    private DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping(ID)
    public ResponseEntity<Resource> getDocumentDetail(@PathVariable(name = "id") Long documentId) throws ResourceNotFoundException {
        return documentService.getDocumentResource(documentId);
    }

}
