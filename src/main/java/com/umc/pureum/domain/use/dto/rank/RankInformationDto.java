package com.umc.pureum.domain.use.dto.rank;


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

    RankerInformationDto myRank;
    List<RankerInformationDto> allRank;
}
