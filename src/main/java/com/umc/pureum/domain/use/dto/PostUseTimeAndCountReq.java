package com.umc.pureum.domain.use.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Time;

@Data
@AllArgsConstructor
public class PostUseTimeAndCountReq {
    private Long user_id;
    private Time use_time; // 일일 사용시간
    private Time count; // 화면 킨 횟수

}