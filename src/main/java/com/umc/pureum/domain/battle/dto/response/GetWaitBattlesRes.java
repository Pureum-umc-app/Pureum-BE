package com.umc.pureum.domain.battle.dto.response;

public interface GetWaitBattlesRes {
    Long getBattleId();
    Long getChallengerId();
    String getChallengerNickname();
    String getChallengerProfileImg();
    Long getChallengedId();
    String getChallengedNickname();
    String getChallengedProfileImg();
    Long getKeywordId();
    String getKeyword();
    int getDuration();
}
