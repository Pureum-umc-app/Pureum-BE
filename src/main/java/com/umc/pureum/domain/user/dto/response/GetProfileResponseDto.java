package com.umc.pureum.domain.user.dto.response;


import com.umc.pureum.domain.user.entity.mapping.UserProfileMapping;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProfileResponseDto {
    String nickname;
    String profile_photo_url;
    int grade;

    public GetProfileResponseDto(UserProfileMapping userProfileMapping) {
        nickname = userProfileMapping.getNickname();
        profile_photo_url = userProfileMapping.getImage();
        grade = userProfileMapping.getGrade();
    }
}
