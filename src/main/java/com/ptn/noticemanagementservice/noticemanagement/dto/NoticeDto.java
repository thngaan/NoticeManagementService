package com.ptn.noticemanagementservice.noticemanagement.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode
public class NoticeDto {

    private String title;
    private String content;
    private Date registrationDate;
    private Integer numberOfViews;
    private String author;
    private List<DocumentDto> documentDtos;

}
