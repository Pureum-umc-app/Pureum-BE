package com.umc.pureum.domain.battle.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnFinishBattleRes {

    private Long battleId;
    private int situation;
    private Long winnerUserId;
    private Long winnerId;
    private String winnerNickname;
    private String winnerImage;
    private Long loserId;
    private String loserNickname;
    private String loserImage;
    private int duration;

    private Long winnerSentenceId;
    private String winnerSentence;
    private Long loserSentenceId;
    private String loserSentence;

    private Long winnerLikeCnt;
    private Long loserLikeCnt;

    private int userLike;
    private int oppLike;


}
