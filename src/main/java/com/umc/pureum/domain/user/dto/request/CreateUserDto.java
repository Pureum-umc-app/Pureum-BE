package com.umc.pureum.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * 유저 생성 DTO
 */
public class CreateUserDto {
    String nickname;
    String profile_photo;
    int grade;
}
