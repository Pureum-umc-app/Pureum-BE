package com.umc.pureum.domain.sentence.dto.request;

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

    @ApiModelProperty(example = "O")
    private String status;

    public CreateSentenceReq() {

    }
}
