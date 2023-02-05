package com.umc.pureum.domain.attendance.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttendanceCheckRes {
    @ApiModelProperty(example = "5")
    private Long check_id;

    @Builder
    public AttendanceCheckRes(Long check_id){
        this.check_id = check_id;
    }

}
