package com.ptn.noticemanagementservice.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ptn.noticemanagementservice.common.dto.ErrorDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Generic Response
 * This is the unique format that using for api response.
 *
 * @param <T>
 */
@JsonInclude(Include.NON_EMPTY)
@Data
@NoArgsConstructor
public class GenericResponse<T> {

    private Date timestamp;
    private String status;
    private String code;
    private String message;
    private T data;
    private String exception;
    private List<ErrorDto> errors;

}