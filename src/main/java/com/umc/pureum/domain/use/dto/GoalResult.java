package com.umc.pureum.domain.use.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoalResult {
    @ApiModelProperty(example = "2023-01-19")
    private String date;
    @ApiModelProperty(example = "1")
    private int isSuccess;

    @Builder
    public GoalResult(String date, int isSuccess) {
        this.date = date;
        this.isSuccess = isSuccess;
    }
}
