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
    protected ResponseEntity<BaseException> handleCustomException(final BaseException e) {
        log.error("handleCustomException: {}", e.getStatus());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BaseException(e.getStatus()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<BaseException> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new BaseException(BaseResponseStatus.NOT_ALLOW_METHOD));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<BaseException> handleException(final Exception e) {
        log.error("handleException: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseException(BaseResponseStatus.INTERNAL_SERVER_ERROR));
    }

}