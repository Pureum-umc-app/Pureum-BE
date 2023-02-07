package com.umc.pureum.domain.battle.dto.response;

import com.umc.pureum.domain.battle.entity.BattleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor

public class CreateChallengedSentenceRes {
    @ApiModelProperty(example = "5")
    private Long battleSentenceId;

    @ApiModelProperty(example = "5")
    private Long battleId;

    @ApiModelProperty(example = "A")
    private BattleStatus battleStatus;

    @Builder
    public CreateChallengedSentenceRes(Long battleSentenceId ,Long battleId , BattleStatus battleStatus){
        this.battleSentenceId = battleSentenceId;
        this.battleId = battleId;
        this.battleStatus = battleStatus;
    }

}
