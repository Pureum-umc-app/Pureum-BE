package com.umc.pureum.domain.sentence.dto.request;

import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Sentence;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateSentenceReq {

    @ApiModelProperty(example = "1")
    private Long userId;

    @ApiModelProperty(example = "1")
    private Long keywordId;

    @ApiModelProperty(example = "hello")
    private String sentence;

    @ApiModelProperty(example = "A")
    private String status;

    public CreateSentenceReq() {
    }
}
