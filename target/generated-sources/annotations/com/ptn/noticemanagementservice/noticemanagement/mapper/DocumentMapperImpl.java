package com.ptn.noticemanagementservice.noticemanagement.mapper;

import com.ptn.noticemanagementservice.noticemanagement.dto.DocumentDto;
import com.ptn.noticemanagementservice.noticemanagement.entity.Document;
import com.ptn.noticemanagementservice.noticemanagement.request.DocumentRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-13T16:52:38+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class DocumentMapperImpl implements DocumentMapper {

    @Override
    public List<DocumentDto> toDtoList(Collection<Document> entities) {
        if ( entities == null ) {
            return null;
        }

        List<DocumentDto> list = new ArrayList<DocumentDto>( entities.size() );
        for ( Document document : entities ) {
            list.add( toDto( document ) );
        }

        return list;
    }

    @Override
    public Document toEntity(DocumentRequest request) {
        if ( request == null ) {
            return null;
        }

        Document document = new Document();

        document.setId( request.getId() );
        document.setOrder( request.getOrder() );

        return document;
    }

    @Override
    public List<Document> toEntityList(Collection<DocumentRequest> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Document> list = new ArrayList<Document>( entities.size() );
        for ( DocumentRequest documentRequest : entities ) {
            list.add( toEntity( documentRequest ) );
        }

        return list;
    }

    @Override
    public void updateEntity(DocumentRequest request, Document entity) {
        if ( request == null ) {
            return;
        }

        entity.setId( request.getId() );
        entity.setOrder( request.getOrder() );
    }

    @Override
    public DocumentDto toDto(Document document) {
        if ( document == null ) {
            return null;
        }

        DocumentDto documentDto = new DocumentDto();

        documentDto.setDocumentType( document.getContentType() );
        documentDto.setDocumentName( document.getFileName() );
        documentDto.setId( document.getId() );
        if ( document.getOrder() != null ) {
            documentDto.setOrder( String.valueOf( document.getOrder() ) );
        }

        return documentDto;
    }
}
