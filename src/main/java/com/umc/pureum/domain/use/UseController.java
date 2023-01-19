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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * [POST] /uses/useTimeAndCount <- 작명 추천 좀...
     */
    @ResponseBody
    @PostMapping("/useTimeAndCount")
    public BaseResponse<PostUseTimeAndCountRes> saveUseTimeAndCount(@RequestBody PostUseTimeAndCountReq postUseTimeAndCountReq){
        try{
            Long user_id = jwtService.getUserIdx();
            PostUseTimeAndCountRes postUseTimeAndCountRes = useService.saveTimeAndCount(user_id, postUseTimeAndCountReq);
            return new BaseResponse<>(postUseTimeAndCountRes);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 목표 달성 여부 반환 API
     * 캘린더에 O, X로 표시되고 회원가입 이후의 모든 여부를 반환
     * [GET] /uses/{user_idx}/goals/result
     */
//    @ApiOperation("목표 달성 여부 반환")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
//    })
//    @ResponseBody
//    @GetMapping("/{user_idx}/goals/result")
//    public BaseResponse<List<GetGoalResultsRes>> getGoalResults(@PathVariable int user_idx) {
////        try{
////            int userIdxByJwt = jwtService.getUserIdx();
////            if(user_idx != userIdxByJwt){
////                return new BaseResponse<>(INVALID_JWT);
////            }
////            else{
////                List<GetGoalResultsRes> getGoalResultsRes = useProvider.getGoalResults(user_idx);
////                return new BaseResponse<>(getGoalResultsRes);
////            }
////        }catch(BaseException e){
////            return new BaseResponse<>(e.getStatus());
////        }
////        List<GetGoalResultsRes> getGoalResultsRes = useProvider.getGoalResults(user_idx);
////        return new BaseResponse<>(getGoalResultsRes);
//    }
}
