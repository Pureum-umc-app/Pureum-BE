package com.umc.pureum.domain.use.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Getter
@NoArgsConstructor
public class GetRankInformationDto {

    private String nickname;
    private String image;
    private Time useTime;

    @Builder
    public GetRankInformationDto(String nickname, String image, Time useTime){
        this.nickname = nickname;
        this.image = image;
        this.useTime = useTime;
    }
}
