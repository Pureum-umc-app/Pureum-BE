package com.umc.pureum.domain.battle.dto.response;

import com.umc.pureum.domain.battle.entity.BattleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRunBattleRes {

    private Long battleId;
    private Long keywordId;
    private String keyword;
    private String remainDuration;
    private Long challengedId;
    private String challengedNickname;
    private String challengedImage;
    private Long challengerId;
    private String challengerNickname;
    private String challengerImage;
    private int duration;
    private BattleStatus status;

    private Long challengedSentenceId;
    private String challengedSentence;
    private Long challengerSentenceId;
    private String challengerSentence;

    private Long challengedLikeCnt;
    private Long challengerLikeCnt;

    private int challengedLike;
    private int challengerLike;

    private Boolean blamed;
}
