package com.umc.pureum.global.config.SecurityConfig;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
public class CustomAuthDeatils implements AuthenticationDetailsSource<HttpServletRequest, RequestInfo> {
    @Override
    public RequestInfo buildDetails(HttpServletRequest request) {
        return RequestInfo.builder()
                .remoteIp(request.getRemoteAddr())
                .loginTime(LocalDateTime.now())
                .build();
    }
}
