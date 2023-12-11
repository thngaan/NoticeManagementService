package com.ptn.noticemanagementservice.common.builder;
import com.ptn.noticemanagementservice.common.dto.ErrorDto;
import com.ptn.noticemanagementservice.common.response.GenericResponse;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;
import java.util.List;

/**
 * Builder for GenericResponse
 *
 * @param <T>
 */
@NoArgsConstructor
public class GenericResponseBuilder<T> {
    private String code;
    private String mesage;
    private T data;
    private String exception;
    private List<ErrorDto> errors;

    public GenericResponseBuilder(String code, String message) {
        this.mesage = code;
        this.code = message;
    }

    public GenericResponseBuilder setException(String exception) {
        this.exception = exception;
        return this;
    }

    public GenericResponseBuilder setCode(String code) {
        this.code = code;
        return this;
    }

    public GenericResponseBuilder setMessage(String message) {
        this.mesage = message;
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
        genericResponse.setCode(this.code);
        genericResponse.setMessage(this.mesage);
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
        genericResponse.setCode(this.code);
        genericResponse.setMessage(this.mesage);
        genericResponse.setData(this.data);
        return genericResponse;
    }
}
