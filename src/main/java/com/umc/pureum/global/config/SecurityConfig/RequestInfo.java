package com.umc.pureum.global.config.SecurityConfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestInfo {
    private String remoteIp;
    private LocalDateTime loginTime;
}
