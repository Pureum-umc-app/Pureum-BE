package com.umc.pureum.domain.use;


import com.umc.pureum.domain.use.dto.GetGoalResultsRes;
import com.umc.pureum.domain.use.dto.PostUseTimeAndCountReq;
import com.umc.pureum.domain.use.dto.PostUseTimeAndCountRes;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import com.umc.pureum.global.utils.JwtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.umc.pureum.global.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "홈")
@RequestMapping("/uses")
public class UseController {
    private final UseProvider useProvider;
    private final UseService useService;
    private final JwtService jwtService;

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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
    })
    @ResponseBody
    @GetMapping("/{user_idx}/goals/result")
    public BaseResponse<List<GetGoalResultsRes>> getGoalResults(@PathVariable Long user_idx) {
        try{
            Long userIdxByJwt = jwtService.getUserIdx();
            if(!Objects.equals(user_idx, userIdxByJwt)){
                return new BaseResponse<>(INVALID_JWT);
            }
            else{
                List<GetGoalResultsRes> getGoalResultsRes = useProvider.getGoalResults(user_idx);
                return new BaseResponse<>(getGoalResultsRes);
            }
        } catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
