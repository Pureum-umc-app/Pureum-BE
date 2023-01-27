package com.umc.pureum.domain.mypage.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatchEditProfileReq {
    String nickname;
    MultipartFile image;
}
