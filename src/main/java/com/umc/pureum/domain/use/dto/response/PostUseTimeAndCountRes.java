package com.umc.pureum.domain.use.dto.response;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostUseTimeAndCountRes {
    @ApiModelProperty(example = "1")
    private Long useId;
}
