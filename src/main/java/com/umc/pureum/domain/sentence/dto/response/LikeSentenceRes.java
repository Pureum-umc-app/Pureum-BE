package com.umc.pureum.domain.sentence.dto.response;

import com.umc.pureum.global.entity.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class LikeSentenceRes {
    @ApiModelProperty(example = "5")
    private Long sentence_like_id;

    @ApiModelProperty(example = "A")
    private Status status;

    @Builder
    public LikeSentenceRes(Long sentence_like_id, Status status) {
        this.sentence_like_id = sentence_like_id;
        this.status = status;
    }
}
