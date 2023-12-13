package com.ptn.noticemanagementservice.noticemanagement.mapper;

import com.ptn.noticemanagementservice.noticemanagement.dto.DocumentDto;
import com.ptn.noticemanagementservice.noticemanagement.dto.NoticeDto;
import com.ptn.noticemanagementservice.noticemanagement.entity.Document;
import com.ptn.noticemanagementservice.noticemanagement.entity.Notice;
import com.ptn.noticemanagementservice.noticemanagement.request.DocumentRequest;
import com.ptn.noticemanagementservice.noticemanagement.request.NoticeRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-13T14:08:00+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class NoticeMapperImpl implements NoticeMapper {

    @Autowired
    private DocumentMapper documentMapper;

    @Override
    public List<NoticeDto> toDtoList(Collection<Notice> entities) {
        if ( entities == null ) {
            return null;
        }

        List<NoticeDto> list = new ArrayList<NoticeDto>( entities.size() );
        for ( Notice notice : entities ) {
            list.add( toDto( notice ) );
        }

        return list;
    }

    @Override
    public List<Notice> toEntityList(Collection<NoticeRequest> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Notice> list = new ArrayList<Notice>( entities.size() );
        for ( NoticeRequest noticeRequest : entities ) {
            list.add( toEntity( noticeRequest ) );
        }

        return list;
    }

    @Override
    public void updateEntity(NoticeRequest request, Notice entity) {
        if ( request == null ) {
            return;
        }

        entity.setId( request.getId() );
        if ( entity.getDocuments() != null ) {
            List<Document> list = documentRequestListToDocumentList( request.getDocuments() );
            if ( list != null ) {
                entity.getDocuments().clear();
                entity.getDocuments().addAll( list );
            }
            else {
                entity.setDocuments( null );
            }
        }
        else {
            List<Document> list = documentRequestListToDocumentList( request.getDocuments() );
            if ( list != null ) {
                entity.setDocuments( list );
            }
        }
        entity.setStartDate( request.getStartDate() );
        entity.setEndDate( request.getEndDate() );
        entity.setTitle( request.getTitle() );
        entity.setContentDetail( request.getContentDetail() );
    }

    @Override
    public NoticeDto toDto(Notice notice) {
        if ( notice == null ) {
            return null;
        }

        NoticeDto noticeDto = new NoticeDto();

        noticeDto.setRegistrationDate( notice.getCreatedDate() );
        noticeDto.setNumberOfViews( notice.getViewCounter() );
        noticeDto.setDocuments( documentListToDocumentDtoList( notice.getDocuments() ) );
        noticeDto.setId( notice.getId() );
        noticeDto.setTitle( notice.getTitle() );
        noticeDto.setStartDate( notice.getStartDate() );
        noticeDto.setEndDate( notice.getEndDate() );

        return noticeDto;
    }

    @Override
    public NoticeDto toDtoForViewer(Notice notice) {
        if ( notice == null ) {
            return null;
        }

        NoticeDto noticeDto = new NoticeDto();

        noticeDto.setRegistrationDate( notice.getCreatedDate() );
        noticeDto.setNumberOfViews( notice.getViewCounter() );
        noticeDto.setDocuments( documentListToDocumentDtoList( notice.getDocuments() ) );
        noticeDto.setId( notice.getId() );
        noticeDto.setTitle( notice.getTitle() );

        return noticeDto;
    }

    @Override
    public Notice toEntity(NoticeRequest noticeRequest) {
        if ( noticeRequest == null ) {
            return null;
        }

        Notice notice = new Notice();

        notice.setId( noticeRequest.getId() );
        notice.setStartDate( noticeRequest.getStartDate() );
        notice.setEndDate( noticeRequest.getEndDate() );
        notice.setTitle( noticeRequest.getTitle() );
        notice.setContentDetail( noticeRequest.getContentDetail() );

        return notice;
    }

    @Override
    public void updateEntity(Notice notice, NoticeRequest noticeRequest) {
        if ( noticeRequest == null ) {
            return;
        }

        notice.setId( noticeRequest.getId() );
        notice.setStartDate( noticeRequest.getStartDate() );
        notice.setEndDate( noticeRequest.getEndDate() );
        notice.setTitle( noticeRequest.getTitle() );
        notice.setContentDetail( noticeRequest.getContentDetail() );
    }

    protected Document documentRequestToDocument(DocumentRequest documentRequest) {
        if ( documentRequest == null ) {
            return null;
        }

        Document document = new Document();

        document.setId( documentRequest.getId() );
        document.setOrder( documentRequest.getOrder() );

        return document;
    }

    protected List<Document> documentRequestListToDocumentList(List<DocumentRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<Document> list1 = new ArrayList<Document>( list.size() );
        for ( DocumentRequest documentRequest : list ) {
            list1.add( documentRequestToDocument( documentRequest ) );
        }

        return list1;
    }

    protected List<DocumentDto> documentListToDocumentDtoList(List<Document> list) {
        if ( list == null ) {
            return null;
        }

        List<DocumentDto> list1 = new ArrayList<DocumentDto>( list.size() );
        for ( Document document : list ) {
            list1.add( documentMapper.toDto( document ) );
        }

        return list1;
    }
}
