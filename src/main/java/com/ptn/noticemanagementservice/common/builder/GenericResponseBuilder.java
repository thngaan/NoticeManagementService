package com.ptn.noticemanagementservice.common.builder;

import com.ptn.noticemanagementservice.common.dto.ErrorDto;
import com.ptn.noticemanagementservice.common.response.GenericResponse;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Builder for GenericResponse
 */
@NoArgsConstructor
public class GenericResponseBuilder<T> {
    private String message;
    private T data;
    private String exception;
    private List<ErrorDto> errors;

    public GenericResponseBuilder(String message) {
        this.message = message;
    }

    public GenericResponseBuilder setException(String exception) {
        this.exception = exception;
        return this;
    }

    public GenericResponseBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public GenericResponseBuilder setErrors(List<ErrorDto> errors) {
        this.errors = errors;
        return this;
    }

    public GenericResponseBuilder setData(T data) {
        this.data = data;
        return this;
    }

    public GenericResponse<T> buildFailure() {
        GenericResponse<T> genericResponse = new GenericResponse<>();
        genericResponse.setStatus("FAILED");
        genericResponse.setTimestamp(new Date());
        genericResponse.setMessage(this.message);
        if (this.data != null) {
            genericResponse.setData(data);
        }
        genericResponse.setException(this.exception);
        if (!CollectionUtils.isEmpty(this.errors)) {
            genericResponse.setErrors(this.errors);
        }
        return genericResponse;
    }

    public GenericResponse buildSuccess() {
        GenericResponse<T> genericResponse = new GenericResponse<>();
        genericResponse.setStatus("SUCCESS");
        genericResponse.setTimestamp(new Date());
        genericResponse.setMessage(this.message);
        genericResponse.setData(this.data);
        return genericResponse;
    }
}
