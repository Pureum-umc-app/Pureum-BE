package com.umc.pureum.domain.battle.dto.repsonse;

import com.umc.pureum.domain.battle.entity.BattleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class BattleStatusRes {
    @ApiModelProperty(example = "5")
    private Long battleId;

    @ApiModelProperty(example = "A")
    private BattleStatus battleStatus;

    @Builder
    public BattleStatusRes(Long battleId , BattleStatus battleStatus){
        this.battleId = battleId;
        this.battleStatus = battleStatus;
    }

}
