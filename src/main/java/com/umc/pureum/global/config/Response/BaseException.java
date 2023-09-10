package com.umc.pureum.global.config.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private BaseResponseStatus status;  //BaseResponseStatus 객체에 매핑
}
