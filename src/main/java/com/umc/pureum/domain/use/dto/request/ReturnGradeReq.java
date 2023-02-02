package com.umc.pureum.domain.use.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReturnGradeReq {
    private Long userId;

    public ReturnGradeReq() {
    }
}
