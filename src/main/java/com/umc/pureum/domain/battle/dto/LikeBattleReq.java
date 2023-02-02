package com.umc.pureum.domain.battle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeBattleReq {
    private Long sentenceId;

    public LikeBattleReq() {
    }
}
