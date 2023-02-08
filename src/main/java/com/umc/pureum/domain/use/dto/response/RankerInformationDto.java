package com.umc.pureum.domain.use.dto.response;


import com.umc.pureum.domain.use.dto.time.TimeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankerInformationDto {

    private int rankNum;
    private String nickname;
    private String image;
    private TimeInfo useTime;
    private TimeInfo purposeTime;
}
