package com.umc.pureum.domain.use.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Time;

@Data
@AllArgsConstructor
public class GetUseTimeAndCountRes {

    private Time use_time;
    private int count;
}
