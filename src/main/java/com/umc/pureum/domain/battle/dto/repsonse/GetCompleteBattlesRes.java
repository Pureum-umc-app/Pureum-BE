package com.umc.pureum.domain.battle.dto.repsonse;

public interface GetCompleteBattlesRes {
    Long getBattleId();
    Long getWordId();
    String getWord();
    Long getWinnerId();
    String getWinnerNickname();
    String getWinnerProfileImg();
}
