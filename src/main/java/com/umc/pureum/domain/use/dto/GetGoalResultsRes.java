package com.umc.pureum.domain.use.dto;

import io.swagger.annotations.Api;
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
    private List<GoalResult> goalResults;

    @Builder
    public GetGoalResultsRes(Long userId, List<GoalResult> goalResults) {
        this.userId = userId;
        this.goalResults = goalResults;
    }
}
