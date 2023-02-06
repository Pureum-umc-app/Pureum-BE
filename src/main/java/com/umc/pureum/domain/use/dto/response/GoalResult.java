package com.umc.pureum.domain.use.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoalResult {
    @ApiModelProperty(example = "2023-01-19")
    private String date;
    @ApiModelProperty(example = "성공 : 1, 실패 : 0, 목표 사용 시간을 설정하지 않은 경우 : null")
    private Integer isSuccess;

    @Builder
    public GoalResult(String date, Integer isSuccess) {
        this.date = date;
        this.isSuccess = isSuccess;
    }
}
