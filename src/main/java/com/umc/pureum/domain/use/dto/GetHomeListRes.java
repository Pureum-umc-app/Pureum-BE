package com.umc.pureum.domain.use.dto;


import com.umc.pureum.domain.use.dto.rank.RankerInformationDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetHomeListRes {

    @ApiModelProperty(example = "2023-01-19")
    private String date;

    @ApiModelProperty(example = "07:00:00")
    private Time useTime;

    @ApiModelProperty(example = "07:00:00")
    private Time purposeTime;

    @ApiModelProperty(example = "1")
    private int count;
    private List<RankerInformationDto> rank = new ArrayList<>();
}
