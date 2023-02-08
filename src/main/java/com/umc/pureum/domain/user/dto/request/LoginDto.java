package com.umc.pureum.domain.user.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDto {
    String fcmId;
    String kakaoToken;
}
