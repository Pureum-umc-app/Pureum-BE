package com.umc.pureum.domain.use.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankInformationDto {

    private RankerInformationDto myRank;
    private List<RankerInformationDto> allRank;
}
