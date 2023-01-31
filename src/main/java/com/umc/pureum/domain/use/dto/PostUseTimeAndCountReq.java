package com.umc.pureum.domain.use.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUseTimeAndCountReq {
    private String hour; // 일일 사용시간(시)
    private String minute; // 일일 사용시간(분)
    private int count; // 화면 킨 횟수

}
