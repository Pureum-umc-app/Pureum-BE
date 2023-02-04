package com.umc.pureum.domain.sentence.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateSentenceRes {
    @ApiModelProperty(example = "5")
    private Long sentence_id;

    @Builder
    public CreateSentenceRes(Long sentence_id){
        this.sentence_id = sentence_id;
    }
}
