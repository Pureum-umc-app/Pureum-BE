package com.umc.pureum.domain.attendance.dto.response;

import io.swagger.annotations.ApiModelProperty;

public interface GetStampInterface {
    @ApiModelProperty(example = "50")
    int getAccumulatedCnt();
    @ApiModelProperty(example = "20")
    int getCurrentCnt();
}
