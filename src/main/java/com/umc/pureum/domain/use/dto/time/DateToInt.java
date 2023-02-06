package com.umc.pureum.domain.use.dto.time;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateToInt {

    private int year;
    private int month;
    private int day;
}
