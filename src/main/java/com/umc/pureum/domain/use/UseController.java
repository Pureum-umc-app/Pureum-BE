package com.umc.pureum.domain.use;


import com.umc.pureum.domain.use.dto.PostUseTimeAndCountReq;
import com.umc.pureum.domain.use.dto.PostUseTimeAndCountRes;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import com.umc.pureum.global.utils.JwtService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    // postUseTimeAndCountRes 를 안 만들고 String 을 넣은 이유! -> 자동적으로 저장되는 거다보니 결과를 보내는 것보다는 성공했다는 상태 코드(& 메세지)만 보내주면 될 것 같다 판단!
    public BaseResponse<PostUseTimeAndCountRes> saveUseTimeAndCount(@RequestBody PostUseTimeAndCountReq postUseTimeAndCountReq){
        try{
            int userIdx = jwtService.getUserIdx();
            if(userIdx != postUseTimeAndCountReq.getUser_id()){
                return new BaseResponse<>(INVALID_JWT);
            }
            else{
                PostUseTimeAndCountRes postUseTimeAndCountRes = useService.saveTimeAndCount(postUseTimeAndCountReq);
                return new BaseResponse<>(postUseTimeAndCountRes);
            }
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 목표 달성 여부 반환 API
     * 캘린더에 O, X로 표시되고 한달 기준으로 반환함
     * [GET] /uses/goals/result
     */
//    public
}
