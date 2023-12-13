package com.ptn.noticemanagementservice.common.exception;

import com.ptn.noticemanagementservice.common.builder.GenericResponseBuilder;
import com.ptn.noticemanagementservice.common.dto.ErrorDto;
import com.ptn.noticemanagementservice.common.response.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger HANDLER_LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(NoticeException.class)
    public final ResponseEntity<GenericResponse> handleNoticeException(NoticeException ex) {
        HANDLER_LOGGER.error("Message error {}", ex.getMessage());
        return new ResponseEntity<>(new GenericResponseBuilder()
                .setMessage(ex.getMessage())
                .buildFailure(), HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<GenericResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        HANDLER_LOGGER.error("Message error {}", ex.getMessage());
        return new ResponseEntity<>(new GenericResponseBuilder()
                .setMessage(ex.getMessage())
                .buildFailure(), HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    public final ResponseEntity<GenericResponse> handleFileException(HttpServletRequest request, Throwable ex) {
        HANDLER_LOGGER.error("Message error {}", ex.getStackTrace());
        return new ResponseEntity<>(new GenericResponseBuilder<>().setMessage(ex.getMessage()).buildFailure(), BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<GenericResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        HANDLER_LOGGER.error("Message error: {}", ex.getMessage());
        return new ResponseEntity<>(new GenericResponseBuilder<>()
                .setMessage("Account is not found").buildFailure(), UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<GenericResponse> handleBadCredentialsException(BadCredentialsException ex) {
        HANDLER_LOGGER.error("Message error: {}", ex.getMessage());
        return new ResponseEntity<>(new GenericResponseBuilder<>().buildFailure(), UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<GenericResponse> handleAccessDeniedException(Exception exp) {
        HANDLER_LOGGER.error("Message error: {} ", exp.getMessage());
        return new ResponseEntity<>(new GenericResponseBuilder<>().buildFailure(), FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<GenericResponse> handleUncaughtException(Exception ex) {
        HANDLER_LOGGER.error("Message error {}", ex.getStackTrace());
        return new ResponseEntity<>(new GenericResponseBuilder<>().setMessage(ex.getMessage()).buildFailure(), BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HANDLER_LOGGER.error("Message error {}", ex.getStackTrace());
        return new ResponseEntity<>(new GenericResponseBuilder<>().setMessage(ex.getMessage()).buildFailure(), BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        List<ErrorDto> errors = fieldErrors.stream()
                .map(item -> new ErrorDto(item.getField(), StringUtils.hasText(item.getDefaultMessage()) ? item.getDefaultMessage() : ""))
                .collect(Collectors.toList());
        return new ResponseEntity(new GenericResponseBuilder<>().setErrors(errors).buildFailure(), BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode status, WebRequest request) {
        HANDLER_LOGGER.error("Message error {}", ex.getStackTrace());
        return new ResponseEntity<>(new GenericResponseBuilder<>().setMessage(ex.getMessage()).buildFailure(), BAD_REQUEST);
    }

}