package com.ptn.noticemanagementservice.noticemanagement.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class DocumentDto {

    private Long id;
    private String documentName;
    private String documentType;
}
