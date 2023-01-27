package com.umc.pureum.domain.use;


import com.umc.pureum.domain.use.dto.GetGoalResultsRes;
import com.umc.pureum.domain.use.dto.PostUseTimeAndCountReq;
import com.umc.pureum.domain.use.dto.PostUseTimeAndCountRes;
import com.umc.pureum.domain.use.dto.request.SetUsageTimeReq;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.Objects;

import static com.umc.pureum.global.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "홈")
@RequestMapping("/uses")
public class UseController {
    private final UseProvider useProvider;
    private final UseService useService;

    /**
     * 일일 사용 시간, 휴대폰 켠 횟수 저장 API
     * [POST] /uses/{user_id}/useTimeAndCount
     */
    @ApiOperation("일일 사용 시간, 휴대폰 켠 횟수 저장")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "use_time",paramType = "formData",value = "일일 사용 시간"),
            @ApiImplicitParam(name = "count",paramType = "formData",value = "휴대폰 켠 횟수"),
    })
    @ResponseBody
    @PostMapping("/{user_idx}/useTimeAndCount")
    public BaseResponse<PostUseTimeAndCountRes> saveUseTimeAndCount(@PathVariable Long user_idx, @RequestBody PostUseTimeAndCountReq postUseTimeAndCountReq){
        // springSecurity 에서 userId 받아와서 Long 형으로 바꿈
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String springSecurityUserId = principal.getUsername();
        Long userId = Long.parseLong(springSecurityUserId);
        if(userId != user_idx){
            return new BaseResponse<>(INVALID_JWT);
        }
        else{
            PostUseTimeAndCountRes postUseTimeAndCountRes = useService.saveTimeAndCount(user_idx, postUseTimeAndCountReq);
            return new BaseResponse<>(postUseTimeAndCountRes);
        }
    }

    /**
     * 목표 달성 여부 반환 API
     * 캘린더에 O, X로 표시되고 회원가입 이후의 모든 여부를 반환
     * [GET] /uses/{user_idx}/goals/result
     * String date = updated_at - 1
     * int isSuccess = 0, 1
     */
    @ApiOperation("목표 달성 여부 반환")
    @ResponseBody
    @GetMapping("/{userId}/goals/result")
    public BaseResponse<GetGoalResultsRes> getGoalResults(@PathVariable Long userId) {
        try{
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String user = principal.getUsername();

            Long userIdByAuth = Long.parseLong(user);

            if(!Objects.equals(userId, userIdByAuth)){
                return new BaseResponse<>(INVALID_JWT);
            }
            else{
                GetGoalResultsRes getGoalResultsRes = useProvider.getGoalResults(userId);
                return new BaseResponse<>(getGoalResultsRes);
            }
        } catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("목표 사용 시간 설정 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "userId", paramType = "path", value = "유저 인덱스", example = "1"),
            @ApiImplicitParam(name = "SetUsageTimeReq", paramType = "body",value = "목표사용시간")
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다.", response = String.class),
            @ApiResponse(code = 2022, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2051, message = "이미 목표시간을 설정하였습니다.")
    })
    @ResponseBody
    @PostMapping("/{userId}/set_usage_time")
    public ResponseEntity<BaseResponse<String>> setUsageTime(@PathVariable Long userId, @RequestBody SetUsageTimeReq setUsageTimeReq) throws BaseException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user = principal.getUsername();
        long id = Long.parseLong(user);
        if(id!=userId)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponse<>(INVALID_JWT));
        if(useService.existUsageTime(userId))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>(POST_USE_EXISTS_USAGE_TIME));
        useService.setUsageTime(userId,setUsageTimeReq);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>("설정하다"));
    }
}
