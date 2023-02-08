package com.umc.pureum.domain.badge.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ReturnBadgesRes {
    private List<Integer> badges;
    private int badgesCount;

    @Builder
    public ReturnBadgesRes(List<Integer> badges, int badgesCount) {
        this.badges = badges;
        this.badgesCount = badgesCount;
    }
}
