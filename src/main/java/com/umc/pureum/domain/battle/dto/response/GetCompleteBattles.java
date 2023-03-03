package com.umc.pureum.domain.battle.dto.response;

import io.swagger.annotations.ApiModelProperty;

public interface GetCompleteBattles {
    @ApiModelProperty("1")
    Long getBattleId();
    @ApiModelProperty("1")
    Long getWordId();
    @ApiModelProperty("가게")
    String getWord();
    @ApiModelProperty("이긴 경우 : 이긴 사람 유저 인덱스, 비긴 경우 : 0")
    Long getWinnerId();
    @ApiModelProperty("이긴 경우 : 이긴 사람 닉네임, 비긴 경우 : null")
    String getWinnerNickname();
    @ApiModelProperty("이긴 경우 : 이긴 사람 사진, 비긴 경우 : 챌린저 사진")
    String getWinnerProfileImg();
    @ApiModelProperty("이긴 경우 : null, 비긴 경우 : 다른 사람 사진")
    String getOtherProfileImg();
    @ApiModelProperty("비긴 경우 : 0, 이기거나 진 경우 : 1")
    int getHasResult();
}
