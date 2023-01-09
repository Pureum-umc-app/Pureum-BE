package com.umc.pureum.global.config.common.exception;

import com.umc.pureum.global.config.common.api.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}