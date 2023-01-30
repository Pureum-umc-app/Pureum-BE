package com.umc.pureum.domain.attendance.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttendanceCheckReq {
    private Long userId;

    public AttendanceCheckReq() {
    }
}
