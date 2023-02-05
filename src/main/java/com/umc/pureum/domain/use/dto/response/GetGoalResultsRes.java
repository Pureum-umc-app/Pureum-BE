package com.umc.pureum.domain.use.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetGoalResultsRes {
    @ApiModelProperty(example = "1")
    private Long userId;
    private List<GoalResult> goalResultRes;

    @Builder
    public GetGoalResultsRes(Long userId, List<GoalResult> goalResultRes) {
        this.userId = userId;
        this.goalResultRes = goalResultRes;
    }
}
