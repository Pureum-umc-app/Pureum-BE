package com.umc.pureum.domain.use.dto.time;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeInfo {

    @Builder.Default
    private int year = 0;

    @Builder.Default
    private int month = 0;

    @Builder.Default
    private int day = 0;

    @Builder.Default
    private int minutes = 0;
}
