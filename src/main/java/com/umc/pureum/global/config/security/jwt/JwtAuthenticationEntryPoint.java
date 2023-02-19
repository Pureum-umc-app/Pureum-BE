package com.umc.pureum.global.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.pureum.global.config.Response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static com.umc.pureum.global.config.Response.BaseResponseStatus.INVALID_JWT;

/*
인증이 실패했을 때 사용
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        //에러 표시 ( 401, json형태, 인코딩 방식 지정)
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        log.info("JWT Authentication Entry Point {}", response);
        try (OutputStream os = response.getOutputStream()) {
            objectMapper.writeValue(os, new BaseResponse<>(INVALID_JWT));
            os.flush();
        }
    }
}