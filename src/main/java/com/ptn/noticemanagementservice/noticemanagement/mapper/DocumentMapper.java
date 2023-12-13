package com.ptn.noticemanagementservice.noticemanagement.mapper;

import com.ptn.noticemanagementservice.common.mapper.RequestResponseMapper;
import com.ptn.noticemanagementservice.noticemanagement.dto.DocumentDto;
import com.ptn.noticemanagementservice.noticemanagement.entity.Document;
import com.ptn.noticemanagementservice.noticemanagement.request.DocumentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface DocumentMapper extends RequestResponseMapper<DocumentRequest, Document, DocumentDto> {

    @Simple
    @Mapping(target = "documentType", source = "contentType")
    @Mapping(target = "documentName", source = "fileName")
    DocumentDto toDto(Document document);

    @Simple
    Document toEntity(DocumentRequest documentRequest);

    @Simple
    void updateEntity(@MappingTarget Document document, DocumentRequest documentRequest);
}