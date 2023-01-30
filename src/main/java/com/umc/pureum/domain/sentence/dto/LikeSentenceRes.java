package com.umc.pureum.domain.sentence.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class LikeSentenceRes {
    @ApiModelProperty(example = "5")
    private Long sentence_like_id;

    @Builder
    public LikeSentenceRes(Long sentence_like_id){
        this.sentence_like_id = sentence_like_id;
    }
}
