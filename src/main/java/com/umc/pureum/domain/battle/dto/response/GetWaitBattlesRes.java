package com.umc.pureum.domain.battle.dto.response;

import io.swagger.annotations.ApiModelProperty;

public interface GetWaitBattlesRes {
    @ApiModelProperty(example = "5")
    Long getBattleId();
    @ApiModelProperty(example = "수락 전 : W, 수락 후 : A, 진행 중 : I, 종료 : C, 취소 : D")
    String getStatus();
    @ApiModelProperty(example = "26")
    Long getOtherId();
    @ApiModelProperty(example = "피터")
    String getOtherNickname();
    @ApiModelProperty(example = "이미지 주소")
    String getOtherProfileImg();
    @ApiModelProperty(example = "5")
    Long getWordId();
    @ApiModelProperty(example = "가게")
    String getWord();
    @ApiModelProperty(example = "10")
    int getDuration();
}
