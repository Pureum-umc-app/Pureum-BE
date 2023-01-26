package com.umc.pureum.domain.user.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
/*
  유저 생성 DTO
 */
public class CreateUserDto {
    String nickname;
    MultipartFile profile_photo;
    int grade;
}
