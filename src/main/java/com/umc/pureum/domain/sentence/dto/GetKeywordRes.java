package com.umc.pureum.domain.sentence.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetKeywordRes {
    @ApiModelProperty(example = "2023-01-22")
    private String date;
    @ApiModelProperty(example = "5")
    private String keyword;
    @ApiModelProperty(example = "어느 한 곳에서 멀리 떨어져 있지 않다.")
    private String meaning;

    @Builder
    public GetKeywordRes(String date, String keyword, String meaning) {
        this.date = date;
        this.keyword = keyword;
        this.meaning = keyword;
    }
}
