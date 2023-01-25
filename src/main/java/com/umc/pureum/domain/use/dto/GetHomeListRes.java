package com.umc.pureum.domain.use.dto;

import lombok.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class GetHomeListRes {

    private String date;
    private Time useTime;
    private int count;
    private Time purposeTime;
    private List<GetRankInformationDto> rank = new ArrayList<>();

    @Builder
    public GetHomeListRes(String date, Time useTime, int count, Time purposeTime, List<GetRankInformationDto> rank){
        this.date = date;
        this.useTime = useTime;
        this.count = count;
        this.purposeTime = purposeTime;
        this.rank = rank;
    }



}
