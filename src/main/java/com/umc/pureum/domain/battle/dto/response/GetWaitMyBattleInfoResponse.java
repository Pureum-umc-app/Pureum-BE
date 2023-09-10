package com.umc.pureum.domain.battle.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetWaitMyBattleInfoResponse {
    String word;
    String wordMean;

    String userName;
    String userImage;

    String opponentName;
    String opponentImage;
    String opponentSentence;

    int duration;
}
