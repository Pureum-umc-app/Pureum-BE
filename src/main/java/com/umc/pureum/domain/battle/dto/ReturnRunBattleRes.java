package com.umc.pureum.domain.battle.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReturnRunBattleRes {

    private Long battleId;

    private Long challengerId;

    private Long challenger;

    private Long challengedId;


}
