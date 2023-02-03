package com.umc.pureum.domain.attendance;

import com.umc.pureum.domain.attendance.dto.AttendanceCheckReq;
import com.umc.pureum.domain.attendance.dto.AttendanceCheckRes;
import com.umc.pureum.domain.attendance.dto.GetStampRes;
import com.umc.pureum.domain.sentence.dto.LikeSentenceRes;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.umc.pureum.global.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "출석 체크")
@RequestMapping("/attendances")
public class AttendanceController {
    private final AttendanceProvider attendanceProvider;
    private final AttendanceService attendanceService;
    private final AttendanceDao attendanceDao;

    /**
     * 도장 개수 반환 API
     * 전체 도장 개수 & 현재 스탬프지의 도장 개수 반환
     * [GET] /attendances/{userId}
     */
    @ApiOperation("도장 개수 반환 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class)
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2001, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2004, message = "존재하지 않는 유저입니다.")
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

    /**
     * 출석 체크 API
     * [POST] /attendances/check
     */
    @ApiOperation("출석 체크 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
            @ApiImplicitParam(name = "request", paramType = "body", value = "출석 체크 Request", dataTypeClass = AttendanceCheckReq.class)
    })
    @ResponseBody
    @PostMapping("/check")
    public BaseResponse<AttendanceCheckRes> check(@RequestBody AttendanceCheckReq request) throws BaseException {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String UserId = loggedInUser.getName();

        long userId = Long.parseLong(UserId);

        try{
            // springsecurity 로 찾은 userId 랑 request 로 받은 sentence 에서 찾은 userId 비교
            if(userId != request.getUserId()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            else{
                // 출석 체크 저장
                AttendanceCheckRes attendanceCheckRes = attendanceService.checkAttendance(request);
                return new BaseResponse<>(attendanceCheckRes);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
        }

    }

}
