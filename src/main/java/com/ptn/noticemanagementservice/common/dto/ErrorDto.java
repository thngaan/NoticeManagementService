package com.ptn.noticemanagementservice.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Error dto
 * Using for detail validator error
 */
@Data
@AllArgsConstructor
public class ErrorDto {

    private String field;
    private String message;
}