package com.umc.pureum.domain.attendance.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class GetStampRes {
    @ApiModelProperty(example = "1")
    private Long userId;
    @ApiModelProperty(example = "50")
    private int accumulatedCnt;
    @ApiModelProperty(example = "20")
    private int currentCnt;

    @Builder
    public GetStampRes(Long userId, int accumulatedCnt, int currentCnt) {
        this.userId = userId;
        this.accumulatedCnt = accumulatedCnt;
        this.currentCnt = currentCnt;
    }
}
