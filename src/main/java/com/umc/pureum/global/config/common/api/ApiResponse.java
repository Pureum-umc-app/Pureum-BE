package com.umc.pureum.global.config.common.api;

import lombok.Getter;

@Getter
public class ApiResponse<T>{
    private final int statusCode;
    private final String message;
    private final T data;

    public ApiResponse(ResponseCode responseCode, T data) {
        this.statusCode = responseCode.getStatus().value();
        this.message = responseCode.getMessage();
        this.data = data;
    }

    public ApiResponse(ResponseCode responseCode) {
        this.statusCode = responseCode.getStatus().value();
        this.message = responseCode.getMessage();
        this.data = null;
    }
}
