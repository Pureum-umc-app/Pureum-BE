package com.umc.pureum.domain.battle.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostBattleReq {
    @ApiModelProperty(example = "1")
    private Long wordId;
    @ApiModelProperty(example = "5")
    private Long challengerId;
    @ApiModelProperty(example = "20")
    private Long challengedId;
    @ApiModelProperty(example = "푸름 최고")
    private String sentence;
    @ApiModelProperty(example = "10")
    private int duration;

    public PostBattleReq(Long wordId, Long challengerId, Long challengedId, String sentence, int duration) {
        this.wordId = wordId;
        this.challengerId = challengerId;
        this.challengedId = challengedId;
        this.sentence = sentence;
        this.duration = duration;
    }
}
