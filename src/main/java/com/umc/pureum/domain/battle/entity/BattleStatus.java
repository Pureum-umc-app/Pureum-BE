package com.umc.pureum.domain.battle.entity;

public enum BattleStatus {
    //수락 완료(상대 글작성 대기중)
    W("WAIT"),
    //상대 문장 작성 완료 ( 대결 진행중)
    A("ACTIVE"),
    // 대결 취소
    C("CANCEL"),
    // 대결 거절
    D("DELETE");
    final String name;

    BattleStatus(String name) {
        this.name = name;
    }
}