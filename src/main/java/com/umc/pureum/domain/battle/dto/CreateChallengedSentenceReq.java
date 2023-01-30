package com.umc.pureum.domain.battle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateChallengedSentenceReq {

    private Long battleId;
    private Long battleWordId;
    private String sentence;

    public CreateChallengedSentenceReq() {
    }
}
