package com.umc.pureum.domain.battle.dto.response;

import com.umc.pureum.domain.battle.entity.BattleStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReturnRunBattleRes {

    private Long battleId;
    private Long keywordId;
    private String keyword;
    private int remainDuration;
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

    private int selfLike;
    private int oppLike;

    @Builder

    public ReturnRunBattleRes(Long battleId, Long keywordId, String keyword, int remainDuration,
                              Long challengedId, String challengedNickname, String challengedImage,
                              Long challengerId, String challengerNickname, String challengerImage,
                              int duration, BattleStatus status,
                              Long challengedSentenceId, String challengedSentence,
                              Long challengerSentenceId, String challengerSentence,
                              Long challengedLikeCnt, Long challengerLikeCnt, int selfLike, int oppLike) {
        this.battleId = battleId;
        this.keywordId = keywordId;
        this.keyword = keyword;
        this.remainDuration = remainDuration;
        this.challengedId = challengedId;
        this.challengedNickname = challengedNickname;
        this.challengedImage = challengedImage;
        this.challengerId = challengerId;
        this.challengerNickname = challengerNickname;
        this.challengerImage = challengerImage;
        this.duration = duration;
        this.status = status;
        this.challengedSentenceId = challengedSentenceId;
        this.challengedSentence = challengedSentence;
        this.challengerSentenceId = challengerSentenceId;
        this.challengerSentence = challengerSentence;
        this.challengedLikeCnt = challengedLikeCnt;
        this.challengerLikeCnt = challengerLikeCnt;
        this.selfLike = selfLike;
        this.oppLike = oppLike;
    }
}
