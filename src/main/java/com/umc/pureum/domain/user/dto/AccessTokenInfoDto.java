package com.umc.pureum.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Builder
@Setter
/**
 * access token으로 가져온 유저 정보 저장할 dto
 * 일단 회원가입에 사용
 */
public class AccessTokenInfoDto {

    public Boolean profile_nickname_needs_agreement;
    public Boolean profile_image_needs_agreement;
    public Boolean has_email;
    public Boolean email_needs_agreement;
    public Boolean is_email_valid;
    public Boolean is_email_verified;
    public String email;
    public String nickname;
    public String profile_image_url;
    public Boolean is_default_image;
}