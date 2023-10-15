package com.umc.pureum.global.config.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorDTO {
    private final boolean isSuccess;
    private final String message;

    public ErrorDTO(BaseException e) {
        this.isSuccess = e.getStatus().isSuccess();
        this.message = e.getStatus().getMessage();
    }

    public ErrorDTO(BaseResponseStatus baseResponseStatus) {
        this.isSuccess = baseResponseStatus.isSuccess();
        this.message =  baseResponseStatus.getMessage();
    }
}