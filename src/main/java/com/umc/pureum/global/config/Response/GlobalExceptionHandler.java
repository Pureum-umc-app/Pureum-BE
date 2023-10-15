package com.umc.pureum.global.config.Response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ErrorDTO> handleCustomException(final BaseException e) {
        log.error("handleCustomException: {}", e.getStatus());
        final ErrorDTO errorDTO = new ErrorDTO(e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDTO);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorDTO> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException: {}", e.getMessage());
        final ErrorDTO errorDTO = new ErrorDTO(BaseResponseStatus.METHOD_NOT_ALLOWED);
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(errorDTO);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorDTO> handleException(final Exception e) {
        log.error("handleException: {}", e.getMessage());
        final ErrorDTO errorDTO = new ErrorDTO(BaseResponseStatus.SERVER_ERROR);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDTO);
    }

}