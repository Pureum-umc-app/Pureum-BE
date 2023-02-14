package com.umc.pureum.domain.use.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReturnGradeRes {
    @ApiModelProperty(example = "1")
    private Long userId;
    @ApiModelProperty(example = "1")
    private int grade;

    @Builder
    public ReturnGradeRes(Long userId , int grade) {
        this.userId = userId;
        this.grade = grade;
    }
}
