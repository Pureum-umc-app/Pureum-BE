package com.umc.pureum.domain.battle.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetBattleWordRes {

    private Long wordId;
    private String word;
    private String meaning;
}
