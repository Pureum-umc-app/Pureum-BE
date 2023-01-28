package com.umc.pureum.domain.attendance;

import com.umc.pureum.domain.attendance.dto.GetStampRes;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.umc.pureum.global.config.BaseResponseStatus.INVALID_JWT;

@RestController
@RequiredArgsConstructor
@Api(tags = "출석 체크")
@RequestMapping("/attendances")
public class AttendanceController {
    private final AttendanceProvider attendanceProvider;
    private final AttendanceService attendanceService;

    /**
     * 도장 개수 반환 API
     * 전체 도장 개수 & 현재 스탬프지의 도장 개수 반환
     * [GET] /attendances/{userIdx}
     */
    @ApiOperation("도장 개수 반환 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰")
    })
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<GetStampRes> getStamps(@PathVariable Long userId) {
        try{
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String user = principal.getUsername();

            Long userIdByAuth = Long.parseLong(user);

            if(!Objects.equals(userId, userIdByAuth)){
                return new BaseResponse<>(INVALID_JWT);
            }
            else{
                GetStampRes getStampRes = attendanceProvider.getStamps(userId);
                return new BaseResponse<>(getStampRes);
            }
        } catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
