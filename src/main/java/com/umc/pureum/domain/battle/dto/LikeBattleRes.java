package com.umc.pureum.domain.battle.dto;

import com.umc.pureum.global.entity.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LikeBattleRes {
    @ApiModelProperty(example = "5")
    private Long battle_like_id;

    @ApiModelProperty(example = "A")
    private Status status;

    @Builder
    public LikeBattleRes(Long battle_like_id , Status status){
        this.battle_like_id = battle_like_id;
        this.status = status;
    }

}

