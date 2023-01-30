package com.umc.pureum.domain.battle.dto.repsonse;

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
}
