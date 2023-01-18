package com.umc.pureum.domain.use;


import com.umc.pureum.domain.use.dto.PostUseTimeAndCountReq;
import com.umc.pureum.domain.use.dto.PostUseTimeAndCountRes;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import com.umc.pureum.global.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.umc.pureum.global.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
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
}
