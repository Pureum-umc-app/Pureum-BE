package com.umc.pureum.domain.sentence.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetBeforeKeywordRes {
    @ApiModelProperty(example = "2023-01-22")
    private String date;
    @ApiModelProperty(example = "5")
    private String keyword;

    @Builder
    public GetBeforeKeywordRes(String date, String keyword) {
        this.date = date;
        this.keyword = keyword;
    }
}
