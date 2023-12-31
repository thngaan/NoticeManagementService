package com.ptn.noticemanagementservice.noticemanagement.mapper;

import com.ptn.noticemanagementservice.common.mapper.RequestResponseMapper;
import com.ptn.noticemanagementservice.noticemanagement.dto.NoticeDto;
import com.ptn.noticemanagementservice.noticemanagement.entity.Notice;
import com.ptn.noticemanagementservice.noticemanagement.request.NoticeRequest;
import com.ptn.noticemanagementservice.usermanagement.mapper.AccountMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class, AccountMapper.class})
@Component
public interface NoticeMapper extends RequestResponseMapper<NoticeRequest, Notice, NoticeDto> {

    @Simple
    @Mapping(target = "registrationDate", source = "createdDate")
    @Mapping(target = "numberOfViews", source = "viewCounter")
    @Mapping(target = "content", source = "contentDetail")
    @Mapping(target = "documents", qualifiedBy = Simple.class)
    @Mapping(target = "account", qualifiedBy = Simple.class)
    NoticeDto toDto(Notice notice);

    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "registrationDate", source = "createdDate")
    @Mapping(target = "numberOfViews", source = "viewCounter")
    @Mapping(target = "content", source = "contentDetail")
    @Mapping(target = "documents", qualifiedBy = Simple.class)
    @Mapping(target = "account", qualifiedBy = Simple.class)
    NoticeDto toDtoForViewer(Notice notice);

    @Simple
    @Mapping(target = "documents", ignore = true)
    Notice toEntity(NoticeRequest noticeRequest);

    @Mapping(target = "documents", ignore = true)
    void updateEntity(@MappingTarget Notice notice, NoticeRequest noticeRequest);
}