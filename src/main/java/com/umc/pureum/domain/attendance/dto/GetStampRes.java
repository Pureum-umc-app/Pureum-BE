package com.umc.pureum.domain.attendance.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetStampRes {
    @ApiModelProperty(example = "50")
    private int accumulatedCnt;
    @ApiModelProperty(example = "5")
    private int currentCnt;

    @Builder
    public GetStampRes(int accumulatedCnt, int currentCnt) {
        this.accumulatedCnt = accumulatedCnt;
        this.currentCnt = currentCnt;
    }
}
