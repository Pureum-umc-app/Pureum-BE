package com.umc.pureum.domain.attendance;

import com.umc.pureum.domain.attendance.dto.GetStampRes;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import com.umc.pureum.global.utils.JwtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.umc.pureum.global.config.BaseResponseStatus.INVALID_JWT;

@RestController
@RequiredArgsConstructor
@Api(tags = "출석 체크")
@RequestMapping("/attendances")
public class AttendanceController {
    private final AttendanceProvider attendanceProvider;
    private final AttendanceService attendanceService;
    private final JwtService jwtService;

    /**
     * 도장 개수 반환 API
     * 전체 도장 개수 & 현재 스탬프지의 도장 개수 반환
     * [GET] /attendances/{userIdx}
     */
    @ApiOperation("도장 개수 반환 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
    })
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetStampRes> getStamps(@PathVariable Long userIdx) {
        try{
            Long userIdxByJwt = jwtService.getUserIdx();
            if(!Objects.equals(userIdx, userIdxByJwt)){
                return new BaseResponse<>(INVALID_JWT);
            }
            else{
                GetStampRes getStampRes = attendanceProvider.getStamps(userIdx);
                return new BaseResponse<>(getStampRes);
            }
        } catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
