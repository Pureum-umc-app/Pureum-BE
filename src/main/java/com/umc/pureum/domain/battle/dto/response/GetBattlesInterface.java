package com.umc.pureum.domain.battle.dto.response;

public interface GetBattlesInterface {
    Long getBattleId();
    Long getKeywordId();
    String getKeyword();
    Long getChallengerId();
    String getChallengerNickname();
    String getChallengerProfileImg();
    Long getChallengedId();
    String getChallengedNickname();
    String getChallengedProfileImg();
}
