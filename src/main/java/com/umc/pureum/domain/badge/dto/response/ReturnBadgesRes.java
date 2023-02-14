package com.umc.pureum.domain.badge.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ReturnBadgesRes {

    @ApiModelProperty(example = "[1,2,3,4]")
    private List<Integer> badges;

    @ApiModelProperty(example = "1")
    private int badgesCount;

    @Builder
    public ReturnBadgesRes(List<Integer> badges, int badgesCount) {
        this.badges = badges;
        this.badgesCount = badgesCount;
    }
}
