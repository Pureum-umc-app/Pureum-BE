package com.umc.pureum.domain.use.dto.response;


import com.umc.pureum.domain.use.dto.time.TimeInfo;
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
    private TimeInfo date;

    @ApiModelProperty(example = "450")
    private TimeInfo useTime;

    @ApiModelProperty(example = "300")
    private TimeInfo purposeTime;

    @ApiModelProperty(example = "1")
    private int count;
    @Builder.Default
    private final List<RankerInformationDto> rank = new ArrayList<>();
}
