package com.umc.pureum.domain.battle;

import com.umc.pureum.domain.battle.dto.BattleStatusReq;
import com.umc.pureum.domain.battle.dto.BattleStatusRes;
import com.umc.pureum.domain.battle.dto.CreateChallengedSentenceReq;
import com.umc.pureum.domain.battle.dto.CreateChallengedSentenceRes;
import com.umc.pureum.domain.sentence.dto.CreateSentenceReq;
import com.umc.pureum.domain.sentence.dto.CreateSentenceRes;
import com.umc.pureum.domain.sentence.dto.LikeSentenceReq;
import com.umc.pureum.domain.sentence.dto.LikeSentenceRes;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.umc.pureum.global.config.BaseResponseStatus.DATABASE_ERROR;
import static com.umc.pureum.global.config.BaseResponseStatus.INVALID_USER_JWT;


@RestController
@RequiredArgsConstructor
@Api(tags = "대결하기")
@RequestMapping("/battles")
public class BattleController {
    private final BattleProvider battleProvider;
    private final BattleService battleService;
    private final BattleDao battleDao;


    /**
     * 대결 수락 API
     * [POST] /battles/accept
     */
    @ApiOperation("대결 수락 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "BattleStatusReq", paramType = "body", value = "대결 ID")
    })
    @ResponseBody
    @PostMapping("/accept")
    public BaseResponse<BattleStatusRes> acceptBattle(@RequestBody BattleStatusReq request) throws BaseException {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String UserId = loggedInUser.getName();

        long userId = Long.parseLong(UserId);

        try{
            // springsecurity 로 찾은 userId 랑 request 로 받은 battle 에서 battle 받은 사람의 userId 비교
            if(userId != battleDao.findOne(request.getBattleId()).getChallenged().getId()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            else{
                // 대결 상태 저장
                BattleStatusRes battleStatusRes = battleService.accept(request);
                return new BaseResponse<>(battleStatusRes);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
        }

    }

    /**
     * 대결 거절 API
     * [POST] /battles/reject
     */
    @ApiOperation("대결 거절 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "BattleStatusReq", paramType = "body", value = "대결 ID")
    })
    @ResponseBody
    @PostMapping("/reject")
    public BaseResponse<BattleStatusRes> rejectBattle(@RequestBody BattleStatusReq request) throws BaseException {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String UserId = loggedInUser.getName();

        long userId = Long.parseLong(UserId);

        try{
            // springsecurity 로 찾은 userId 랑 request 로 받은 battle 에서 battle 받은 사람의 userId 비교
            if(userId != battleDao.findOne(request.getBattleId()).getChallenged().getId()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            else{
                // 대결 상태 저장
                BattleStatusRes battleStatusRes = battleService.reject(request);
                return new BaseResponse<>(battleStatusRes);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
        }

    }

    /**
     * 대결 취소 API
     * [POST] /battles/cancel
     */
    @ApiOperation("대결 취소 API ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "BattleStatusReq", paramType = "body", value = "대결 ID")
    })
    @ResponseBody
    @PostMapping("/cancel")
    public BaseResponse<BattleStatusRes> cancelBattle(@RequestBody BattleStatusReq request) throws BaseException {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String UserId = loggedInUser.getName();

        long userId = Long.parseLong(UserId);

        try{
            // springsecurity 로 찾은 userId 랑 request 로 받은 battle 에서 battle 받은 사람의 userId 비교
            if(userId != battleDao.findOne(request.getBattleId()).getChallenged().getId() ||
                    userId != battleDao.findOne(request.getBattleId()).getChallenger().getId()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            else{
                // 대결 상태 저장
                BattleStatusRes battleStatusRes = battleService.reject(request);
                return new BaseResponse<>(battleStatusRes);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
        }

    }

    /**
     * 대결 받은 사람 대결 문장 작성 API
     * [POST] /battles/challenged/write
     */
    @ApiOperation("대결 받은 사람 대결 문장 작성 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "CreateSentenceReq", paramType = "body", value = "문장 작성 Request")
    })
    @ResponseBody
    @PostMapping("/challenged/write")
    public BaseResponse<CreateChallengedSentenceRes> writeSentence(@RequestBody CreateChallengedSentenceReq request) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String UserId = loggedInUser.getName();

        long userId = Long.parseLong(UserId);

        try{
            // springsecurity 로 찾은 userId 랑 request 로 받은 battle 에서 battle 받은 사람의 userId 비교
            if(userId != battleDao.findOne(request.getBattleId()).getChallenged().getId()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            else{
                // challenged 가 작성한 문장 저장
                CreateChallengedSentenceRes createChallengedSentenceRes = battleService.writeChallenged(userId , request);
                return new BaseResponse<>(createChallengedSentenceRes);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
        }

    }

}
