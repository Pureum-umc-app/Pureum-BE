package com.umc.pureum.domain.use.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetUserAccountDto {

    private String nickname;
    private String image;
    private String useTime;

}
