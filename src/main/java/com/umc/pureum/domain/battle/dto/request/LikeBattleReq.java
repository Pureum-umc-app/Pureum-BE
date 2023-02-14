package com.umc.pureum.domain.battle.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeBattleReq {

    @ApiModelProperty(example = "1")
    private Long userId;

    @ApiModelProperty(example = "1")
    private Long sentenceId;

    public LikeBattleReq() {
    }
}
