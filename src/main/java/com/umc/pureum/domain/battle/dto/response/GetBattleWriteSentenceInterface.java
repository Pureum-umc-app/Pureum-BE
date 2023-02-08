package com.umc.pureum.domain.battle.dto.response;

import com.umc.pureum.domain.battle.entity.Battle;
import com.umc.pureum.domain.battle.entity.BattleStatus;
import com.umc.pureum.domain.battle.entity.BattleWord;
import com.umc.pureum.global.entity.Status;

public interface GetBattleWriteSentenceInterface {

    Battle getBattle();
    BattleWord getBattleWord();
    String getKeyword();
    BattleStatus getBattleStatus();
    Status getChallengedStatus();
    Status getChallengerStatus();
}
