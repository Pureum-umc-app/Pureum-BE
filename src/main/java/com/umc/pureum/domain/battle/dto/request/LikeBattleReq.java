package com.umc.pureum.domain.battle.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeBattleReq {
    private Long userId;
    private Long sentenceId;

    public LikeBattleReq() {
    }
}
