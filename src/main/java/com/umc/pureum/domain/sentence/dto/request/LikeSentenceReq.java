package com.umc.pureum.domain.sentence.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeSentenceReq {

    @ApiModelProperty(example = "1")
    private Long userId;

    @ApiModelProperty(example = "1")
    private Long sentenceId;

    public LikeSentenceReq() {
    }
}
