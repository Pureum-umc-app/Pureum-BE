package com.umc.pureum.global.config.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    //TEST_Success
    TEST_SUCCESS(HttpStatus.OK,"테스트 성공");
    private final HttpStatus status;
    private final String message;

}
