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
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
        Notice notice = getById(id);
        if (authenticationFacade.isSameAccount(notice.getAccount())) {
            return noticeMapper.toDto(notice);
        } else {
            ValidationUtils.validateActiveTime(notice.getStartDate(), notice.getEndDate());
//            try {
//                // test multi thread
//                Thread.sleep(10000);
//            } catch (Exception e) {
//
//            }

            noticeRepository.incrementViewCounter(id);
            Notice updatedNotice = noticeRepository.findByIdAndIsDeletedIsFalse(id).get();
            LOGGER.info("----------------- {}", updatedNotice.getViewCounter());
            return noticeMapper.toDtoForViewer(updatedNotice);
        }

    }

    @Override
    public NoticeDto createUpdate(NoticeRequest noticeRequest, List<MultipartFile> attachments) throws ResourceNotFoundException, AccessDeniedException {
        Notice notice;
        Long noticeId = noticeRequest.getId();
        if (noticeId == null) {
            notice = noticeMapper.toEntity(noticeRequest);
            notice.setAccount(authenticationFacade.getAuthenticationAccount());
        } else {
            notice = noticeRepository.findByIdAndIsDeletedIsFalse(noticeId).orElseThrow(() -> {
                LOGGER.error("Notice id {} is deleted", noticeId);
                return new ResourceNotFoundException(Notice.class, noticeId);
            });
            if (authenticationFacade.isSameAccount(notice.getAccount())) {
                noticeMapper.updateEntity(notice, noticeRequest);
            } else {
                LOGGER.error("Username = {} do not allow to resource id {}", notice.getAccount().getUsername(), noticeId);
                throw new AccessDeniedException(String.format("Username = %s do not allow to resource id %s", notice.getAccount().getUsername(), noticeId));
            }
        }
        if (!CollectionUtils.isEmpty(noticeRequest.getDocuments())) {
            notice.setDocuments(initDocuments(notice, noticeRequest.getDocuments(), attachments));
        }
        Notice noticeResponse = save(notice);
        return noticeMapper.toDto(noticeResponse);
    }

    private List<Document> initDocuments(Notice notice, List<DocumentRequest> documentRequests, List<MultipartFile> attachments) throws ResourceNotFoundException {
        Map<String, MultipartFile> map = attachments.stream()
                .collect(Collectors.toMap(MultipartFile::getOriginalFilename, Function.identity()));

        List<Document> documents = new ArrayList<>();
        for (DocumentRequest documentRequest : documentRequests) {
            Document document;
            MultipartFile file = map.get(documentRequest.getFilename());
            if (notice.getId() == null || documentRequest.getId() == null) {
                document = new Document();
                if (file == null) {
                    LOGGER.error("File {} is not existed in attachments.", documentRequest.getFilename());
                    throw new NoticeException(String.format("File %s is not existed in attachments.", documentRequest.getFilename()));
                }
            } else {
                document = documentService.findById(documentRequest.getId());
            }

            if (file != null) {
                document.setContentType(file.getContentType());
                document.setFileSize(file.getSize());
                String fileName = file.getOriginalFilename().replaceAll("[\\\\/:*?\"<>|]", "_");
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

            documents.add(document);
        }
        return documents;
    }

    @Override
    public void softDelete(Long id) throws ResourceNotFoundException, AccessDeniedException {
        Notice notice = getById(id);

        if (authenticationFacade.isSameAccount(notice.getAccount())) {
            notice.setDeleted(true);
            save(notice);
        } else {
            LOGGER.error("Username = {} do not allow to resource id {}", notice.getAccount().getUsername(), id);
            throw new AccessDeniedException(String.format("Username = %s do not allow to resource id %s", notice.getAccount().getUsername(), id));
        }

    }

    @Cacheable(value = "notices", key = "#id")
    public Notice getById(Long id) throws ResourceNotFoundException {
        return noticeRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(() -> {
            LOGGER.error("Notice id {} is not found or notice is deleted", id);
            return new ResourceNotFoundException(Notice.class, id);
        });

    }

    @CachePut(value = "notices", key = "#result.id")
    public Notice save(Notice notice) {
        return noticeRepository.save(notice);
    }


    @Override
    public void hardDelete(Long id) {
        noticeRepository.deleteById(id);
    }
}