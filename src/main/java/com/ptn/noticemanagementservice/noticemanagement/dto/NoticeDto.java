package com.ptn.noticemanagementservice.noticemanagement.dto;

import com.ptn.noticemanagementservice.usermanagement.dto.AccountDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode
public class NoticeDto {

    private Long id;
    private String title;
    private String content;
    private Date registrationDate;
    private Integer numberOfViews;
    private String author;
    private Date startDate;
    private Date endDate;
    private AccountDto account;
    private List<DocumentDto> documents;

}
