package com.umc.pureum.domain.battle.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BattleMyProfilePhotoRes {
    long userId;
    String image;
    String nickname;
}
