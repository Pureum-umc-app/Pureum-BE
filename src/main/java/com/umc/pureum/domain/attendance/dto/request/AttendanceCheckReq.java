package com.umc.pureum.domain.attendance.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttendanceCheckReq {
    private Long userId;

    public AttendanceCheckReq() {
    }
}
