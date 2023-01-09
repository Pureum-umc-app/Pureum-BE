package com.umc.pureum.domain.test.controller;

import com.umc.pureum.global.config.common.api.ApiResponse;
import com.umc.pureum.global.config.common.api.ErrorCode;
import com.umc.pureum.global.config.common.api.ResponseCode;
import com.umc.pureum.global.config.common.exception.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/success")
    public ResponseEntity testSuccess() {
        String data = "test api success";
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(ResponseCode.TEST_SUCCESS, data));
    }

    @GetMapping("/fail")
    public ResponseEntity<ApiResponse> testFail() {
        String data = "test api fail";
        throw new CustomException(ErrorCode.BAD_REQUEST);
    }
}
