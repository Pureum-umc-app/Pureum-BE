package com.umc.pureum.domain.battle.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetBattlesRes {
    private Long battleId;
    private Long keywordId;
    private String keyword;
    private Long challengerId;
    private String challengerNickname;
    private String challengerProfileImg;
    private int isChallengerLike;
    private Long challengerLikeCnt;
    private Long challengedId;
    private String challengedNickname;
    private String challengedProfileImg;
    private int isChallengedLike;
    private Long challengedLikeCnt;

    @Builder
    public GetBattlesRes(Long battleId, Long keywordId, String keyword,
                         Long challengerId, String challengerNickname, String challengerProfileImg,
                         int isChallengerLike, Long challengerLikeCnt,
                         Long challengedId, String challengedNickname, String challengedProfileImg,
                         int isChallengedLike, Long challengedLikeCnt) {
        this.battleId = battleId;
        this.keywordId = keywordId;
        this.keyword = keyword;
        this.challengerId = challengerId;
        this.challengerNickname = challengerNickname;
        this.challengerProfileImg = challengerProfileImg;
        this.isChallengerLike = isChallengerLike;
        this.challengerLikeCnt = challengerLikeCnt;
        this.challengedId = challengedId;
        this.challengedNickname = challengedNickname;
        this.challengedProfileImg = challengedProfileImg;
        this.isChallengedLike = isChallengedLike;
        this.challengedLikeCnt = challengedLikeCnt;
    }
}
