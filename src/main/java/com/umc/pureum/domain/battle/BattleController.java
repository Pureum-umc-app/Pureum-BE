package com.umc.pureum.domain.battle;

import com.umc.pureum.domain.battle.dto.PostBattleReq;
import com.umc.pureum.domain.battle.dto.repsonse.BattleMyProfilePhotoRes;
import com.umc.pureum.domain.battle.dto.repsonse.GetWaitBattlesRes;
import com.umc.pureum.domain.use.dto.GetGoalResultsRes;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.umc.pureum.global.config.BaseResponseStatus.*;


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
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2004, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 2050, message = "대결 문장을 입력해야 합니다."),
            @ApiResponse(code = 2051, message = "존재하지 않는 키워드입니다."),
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

            Long battleId = battleService.createBattle(postBattleReq);
            return new BaseResponse<>(battleId);
        }
        catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    @ApiOperation("대결 신청할때 내사진 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "userId", paramType = "path", value = "유저 인덱스")
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다.", response = BattleMyProfilePhotoRes.class),
            @ApiResponse(code = 2022, message = "유효하지 않은 JWT입니다."),
    })
    @ResponseBody
    @GetMapping("/apply/photo/{userId}")
    public ResponseEntity<BaseResponse<BattleMyProfilePhotoRes>> BattleMyProfilePhoto(@PathVariable long userId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id = Long.parseLong(principal.getUsername());
        if (id != userId)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponse<>(INVALID_JWT));
        BattleMyProfilePhotoRes battleMyProfilePhotoRes = new BattleMyProfilePhotoRes(userId,battleService.BattleMyProfilePhoto(userId));
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(battleMyProfilePhotoRes));
    }

    @ApiOperation("대기 중인 대결 리스트 반환")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰")
    })
    @ResponseBody
    @GetMapping("/wait-list/{userId}")
    public BaseResponse<List<GetWaitBattlesRes>> getWaitBattles(@PathVariable Long userId) {
        try {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String user = principal.getUsername();

            Long userIdByAuth = Long.parseLong(user);

            if(!Objects.equals(userId, userIdByAuth)){
                return new BaseResponse<>(INVALID_JWT);
            }

            List<GetWaitBattlesRes> battlesRes = battleProvider.getWaitBattles(userId);
            return new BaseResponse<>(battlesRes);
        }
        catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
