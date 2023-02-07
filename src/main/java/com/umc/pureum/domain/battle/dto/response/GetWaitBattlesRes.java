package com.umc.pureum.domain.battle.dto.response;

public interface GetWaitBattlesRes {
    Long getBattleId();
    Long getOtherId();
    String getOtherNickname();
    String getOtherProfileImg();
    Long getWordId();
    String getWord();
    int getDuration();
}
