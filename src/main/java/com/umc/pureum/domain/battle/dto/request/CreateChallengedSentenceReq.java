package com.umc.pureum.domain.battle.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateChallengedSentenceReq {

    private Long battleId;
    private String sentence;

    public CreateChallengedSentenceReq() {
    }
}
