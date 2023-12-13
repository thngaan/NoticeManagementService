package com.ptn.noticemanagementservice.noticemanagement.service.impl;

import com.ptn.noticemanagementservice.common.exception.NoticeException;
import com.ptn.noticemanagementservice.common.exception.ResourceNotFoundException;
import com.ptn.noticemanagementservice.common.utils.ValidationUtils;
import com.ptn.noticemanagementservice.noticemanagement.dto.NoticeDto;
import com.ptn.noticemanagementservice.noticemanagement.entity.Document;
import com.ptn.noticemanagementservice.noticemanagement.entity.Notice;
import com.ptn.noticemanagementservice.noticemanagement.mapper.NoticeMapper;
import com.ptn.noticemanagementservice.noticemanagement.repository.NoticeRepository;
import com.ptn.noticemanagementservice.noticemanagement.request.DocumentRequest;
import com.ptn.noticemanagementservice.noticemanagement.request.NoticeRequest;
import com.ptn.noticemanagementservice.noticemanagement.service.DocumentService;
import com.ptn.noticemanagementservice.noticemanagement.service.NoticeService;
import com.ptn.noticemanagementservice.usermanagement.service.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NoticeServiceImpl implements NoticeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeServiceImpl.class);

    private final NoticeRepository noticeRepository;
    private final DocumentService documentService;
    private final AuthenticationFacade authenticationFacade;
    private final NoticeMapper noticeMapper;

    public NoticeServiceImpl(NoticeRepository noticeRepository, DocumentService documentService, AuthenticationFacade authenticationFacade, NoticeMapper noticeMapper) {
        this.noticeRepository = noticeRepository;
        this.documentService = documentService;
        this.authenticationFacade = authenticationFacade;
        this.noticeMapper = noticeMapper;
    }

    @Override
    public NoticeDto get(Long id) throws ResourceNotFoundException {
        Notice notice = noticeRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(() -> {
            LOGGER.error("Notice id {} is not found or notice is deleted", id);
            return new ResourceNotFoundException(Notice.class, id);
        });

        // if author, return full data
        if (authenticationFacade.isSameAccount(notice.getAccount())) {
            return noticeMapper.toDto(notice);
        } else { // if viewer, check notice active, ++ viewCounter and return view content only
            ValidationUtils.validateActiveTime(notice.getStartDate(), notice.getEndDate());

            noticeRepository.incrementViewCounter(id);
            Notice updatedNotice = noticeRepository.findByIdAndIsDeletedIsFalse(id).get();
            LOGGER.info("----------------- {}", updatedNotice.getViewCounter());
            return noticeMapper.toDtoForViewer(updatedNotice);
        }
    }

    @Transactional
    @Override
    public NoticeDto create(NoticeRequest noticeRequest, List<MultipartFile> attachments) throws ResourceNotFoundException {
        Notice notice = noticeMapper.toEntity(noticeRequest);
        notice.setAccount(authenticationFacade.getAuthenticationAccount());

        if (!CollectionUtils.isEmpty(noticeRequest.getDocuments())) {
            List<Document> documents = new ArrayList<>();
            Map<String, MultipartFile> map = attachments.stream()
                    .collect(Collectors.toMap(MultipartFile::getOriginalFilename, Function.identity()));
            for (DocumentRequest documentRequest : noticeRequest.getDocuments()) {
                Document document = getDocument(notice, map, documentRequest);
                documents.add(document);
            }
            notice.setDocuments(documents);
        }
        Notice noticeResponse = noticeRepository.save(notice);
        return noticeMapper.toDto(noticeResponse);
    }

    @Transactional
    @Override
    public NoticeDto update(NoticeRequest noticeRequest, List<MultipartFile> attachments) throws ResourceNotFoundException, AccessDeniedException {

        Long noticeId = noticeRequest.getId();
        Notice notice = noticeRepository.findByIdAndIsDeletedIsFalse(noticeId).orElseThrow(() -> {
            LOGGER.error("Notice id {} is deleted", noticeId);
            return new ResourceNotFoundException(Notice.class, noticeId);
        });

        if (authenticationFacade.isSameAccount(notice.getAccount())) {
            noticeMapper.updateEntity(notice, noticeRequest);
        } else {
            LOGGER.error("Username = {} do not allow to resource id {}", notice.getAccount().getUsername(), noticeId);
            throw new AccessDeniedException(String.format("Username = %s do not allow to resource id %s", notice.getAccount().getUsername(), noticeId));
        }

        if (!CollectionUtils.isEmpty(noticeRequest.getDocuments())) {
            List<Document> documents = new ArrayList<>();
            Map<String, MultipartFile> map = attachments.stream()
                    .collect(Collectors.toMap(MultipartFile::getOriginalFilename, Function.identity()));
            // update document to existed.
            Map<Long, DocumentRequest> keepDoc = noticeRequest.getDocuments().stream().collect(Collectors.toMap(DocumentRequest::getId, Function.identity()));

            for (Document document : notice.getDocuments()) {
                if (keepDoc.containsKey(document.getId())) {
                    document.setOrder(keepDoc.get(document.getId()).getOrder());
                    documents.add(document);
                }
            }

            List<DocumentRequest> newDoc = noticeRequest.getDocuments().stream()
                    .filter(item -> item.getId() == null)
                    .collect(Collectors.toList());

            for (DocumentRequest documentRequest : newDoc) {
                Document document = getDocument(notice, map, documentRequest);
                documents.add(document);
            }
            notice.getDocuments().clear();
            notice.getDocuments().addAll(documents);
        }

        return noticeMapper.toDto(noticeRepository.saveAndFlush(notice));
    }

    private Document getDocument(Notice notice, Map<String, MultipartFile> map, DocumentRequest documentRequest) {
        Document document = new Document();
        MultipartFile file = map.get(documentRequest.getFileName());

        if (file != null) {
            document.setContentType(file.getContentType());
            document.setFileSize(file.getSize());
            String fileName = documentRequest.getFileName().replaceAll("[\\\\/:*?\"<>|]", "_");
            document.setFileName(fileName);
            try {
                document.setFileContent(file.getBytes());
            } catch (IOException e) {
                LOGGER.error("Exception when set file content. message = {}", e.getMessage());
                throw new NoticeException("Exception when set file content.", e);
            }
        }

        document.setNotice(notice);
        document.setOrder(documentRequest.getOrder());
        return document;
    }

    @Transactional
    @Override
    public void softDelete(Long id) throws ResourceNotFoundException, AccessDeniedException {
        Notice notice = noticeRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(() -> {
            LOGGER.error("Notice id {} is not found or notice is deleted", id);
            return new ResourceNotFoundException(Notice.class, id);
        });

        if (authenticationFacade.isSameAccount(notice.getAccount())) {
            notice.setDeleted(true);
            noticeRepository.save(notice);
        } else {
            LOGGER.error("Username = {} do not allow to resource id {}", notice.getAccount().getUsername(), id);
            throw new AccessDeniedException(String.format("Username = %s do not allow to resource id %s", notice.getAccount().getUsername(), id));
        }

    }

    @Transactional
    @Override
    public void hardDelete(Long id) {
        noticeRepository.deleteById(id);
    }
}