package com.umc.pureum.domain.battle.dto.repsonse;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BattleFighterRes {

    private Long userId;
    private String nickname;
    private String image;

}
