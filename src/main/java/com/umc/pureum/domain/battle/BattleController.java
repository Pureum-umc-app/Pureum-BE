package com.umc.pureum.domain.battle;

import com.umc.pureum.domain.battle.dto.PostBattleReq;
import com.umc.pureum.domain.use.dto.GetGoalResultsRes;
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
import static com.umc.pureum.global.config.BaseResponseStatus.POST_BATTLE_EMPTY_SENTENCE;


@RestController
@RequiredArgsConstructor
@Api(tags = "대결하기")
@RequestMapping("/battles")
public class BattleController {
    private final BattleProvider battleProvider;
    private final BattleService battleService;

    /**
     * 대결 신청 API
     * 대결 정보를 받아와서 테이블에 저장
     */
    @ApiOperation("대결 신청")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰")
    })
    @ResponseBody
    @PostMapping("")
    public BaseResponse<Long> createBattle(@RequestBody PostBattleReq postBattleReq) {
        try {
            // 문장이 비어있는지 검사
            if(postBattleReq.getSentence().isBlank()) {
                return new BaseResponse<>(POST_BATTLE_EMPTY_SENTENCE);
            }

            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String user = principal.getUsername();

            Long userIdByAuth = Long.parseLong(user);

            if(!Objects.equals(postBattleReq.getChallengerId(), userIdByAuth)){
                return new BaseResponse<>(INVALID_JWT);
            }
            else{
                Long battleId = battleService.createBattle(postBattleReq);
                return new BaseResponse<>(battleId);
            }
        }
        catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
