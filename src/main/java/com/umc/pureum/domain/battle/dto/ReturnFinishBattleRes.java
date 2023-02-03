package com.umc.pureum.domain.battle.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReturnFinishBattleRes {

    private Long battleId;
    private Long userId;
    private Long challengedId;
    private String challengedNickname;
    private String challengedImage;
    private Long challengerId;
    private String challengerNickname;
    private String challengerImage;

    private Long challengedSentenceId;
    private String challengedSentence;
    private Long challengerSentenceId;
    private String challengerSentence;

    private Long challengedLikeCnt;
    private Long challengerLikeCnt;

    private int userLike;

    @Builder
    public ReturnFinishBattleRes(Long battleId, Long userId,
                                 Long challengedId, String challengedNickname, String challengedImage,
                                 Long challengerId, String challengerNickname, String challengerImage,
                                 Long challengedSentenceId, String challengedSentence,
                                 Long challengerSentenceId, String challengerSentence,
                                 Long challengedLikeCnt, Long challengerLikeCnt, int userLike) {
        this.battleId = battleId;
        this.userId = userId;
        this.challengedId = challengedId;
        this.challengedNickname = challengedNickname;
        this.challengedImage = challengedImage;
        this.challengerId = challengerId;
        this.challengerNickname = challengerNickname;
        this.challengerImage = challengerImage;
        this.challengedSentenceId = challengedSentenceId;
        this.challengedSentence = challengedSentence;
        this.challengerSentenceId = challengerSentenceId;
        this.challengerSentence = challengerSentence;
        this.challengedLikeCnt = challengedLikeCnt;
        this.challengerLikeCnt = challengerLikeCnt;
        this.userLike = userLike;
    }
}
