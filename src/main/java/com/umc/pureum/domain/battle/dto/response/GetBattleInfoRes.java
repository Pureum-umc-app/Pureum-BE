package com.umc.pureum.domain.battle.dto.response;

import com.umc.pureum.domain.battle.entity.BattleStatus;

import java.sql.Timestamp;

public interface GetBattleInfoRes {
    Long getBattleId();
    Long getKeywordId();
    String getKeyword();
    Timestamp getUpdateAt();
    Long getChallengedId();
    String getChallengedNickname();
    String getChallengedProfileImg();
    Long getChallengerId();
    String getChallengerNickname();
    String getChallengerProfileImg();
    int getDuration();
    BattleStatus getBattleStatus();
}
