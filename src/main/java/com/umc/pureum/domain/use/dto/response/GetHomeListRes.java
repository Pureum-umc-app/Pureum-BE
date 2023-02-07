package com.umc.pureum.domain.use.dto.response;


import com.umc.pureum.domain.use.dto.time.DateToInt;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetHomeListRes {

    @ApiModelProperty(example = "2023-01-19")
    private DateToInt date;

    @ApiModelProperty(example = "450")
    private int useTime;

    @ApiModelProperty(example = "300")
    private int purposeTime;

    @ApiModelProperty(example = "1")
    private int count;
    private List<RankerInformationDto> rank = new ArrayList<>();
}
