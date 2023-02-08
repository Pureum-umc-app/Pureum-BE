package com.umc.pureum.domain.battle;


import com.umc.pureum.domain.battle.dao.BattleDao;
import com.umc.pureum.domain.battle.dao.BattleSentenceDao;
import com.umc.pureum.domain.battle.dto.response.ReturnFinishBattleRes;
import com.umc.pureum.domain.battle.dto.response.*;
import com.umc.pureum.domain.battle.dto.request.BattleStatusReq;
import com.umc.pureum.domain.battle.dto.request.CreateChallengedSentenceReq;
import com.umc.pureum.domain.battle.dto.request.LikeBattleReq;
import com.umc.pureum.domain.battle.dto.request.PostBattleReq;
import com.umc.pureum.domain.notification.FirebaseCloudMessageService;
import com.umc.pureum.domain.user.UserDao;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
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
    private final BattleDao battleDao;
    private final BattleSentenceDao battleSentenceDao;
    private final UserDao userDao;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    /**
     * 대결 수락 API
     * [POST] /battles/accept
     */
    @ApiOperation("대결 수락 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
            @ApiImplicitParam(name = "request", paramType = "body", value = "대결 ID", dataTypeClass = BattleStatusReq.class)
    })
    @ResponseBody
    @PostMapping("/accept")
    public BaseResponse<BattleStatusRes> acceptBattle(@RequestBody BattleStatusReq request) throws BaseException {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String UserId = loggedInUser.getName();

        long userId = Long.parseLong(UserId);

        try {
            // springsecurity 로 찾은 userId 랑 request 로 받은 battle 에서 battle 받은 사람의 userId 비교
            if (userId != battleDao.findOne(request.getBattleId()).getChallenged().getId()) {
                return new BaseResponse<>(INVALID_USER_JWT);
            } else if (!"A".equals(battleDao.findOne(request.getBattleId()).getChallenged().getStatus())) {
                return new BaseResponse<>(INVALID_USER);
            } else {
                // 대결 상태 저장
                BattleStatusRes battleStatusRes = battleService.accept(request);
                return new BaseResponse<>(battleStatusRes);
            }
        } catch (Exception e) {
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
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
            @ApiImplicitParam(name = "request", paramType = "body", value = "대결 ID", dataTypeClass = BattleStatusReq.class)
    })
    @ResponseBody
    @PostMapping("/reject")
    public BaseResponse<BattleStatusRes> rejectBattle(@RequestBody BattleStatusReq request) throws BaseException {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String UserId = loggedInUser.getName();

        long userId = Long.parseLong(UserId);

        try {
            // springsecurity 로 찾은 userId 랑 request 로 받은 battle 에서 battle 받은 사람의 userId 비교
            if (userId != battleDao.findOne(request.getBattleId()).getChallenged().getId()) {
                return new BaseResponse<>(INVALID_USER_JWT);
            } else if (!"A".equals(battleDao.findOne(request.getBattleId()).getChallenged().getStatus())) {
                return new BaseResponse<>(INVALID_USER);
            } else {
                // 대결 상태 저장
                BattleStatusRes battleStatusRes = battleService.reject(request);
                return new BaseResponse<>(battleStatusRes);
            }
        } catch (Exception e) {
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
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
            @ApiImplicitParam(name = "request", paramType = "body", value = "대결 ID", dataTypeClass = BattleStatusReq.class)
    })
    @ResponseBody
    @PostMapping("/cancel")
    public BaseResponse<BattleStatusRes> cancelBattle(@RequestBody BattleStatusReq request) throws BaseException {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String UserId = loggedInUser.getName();

        long userId = Long.parseLong(UserId);

        try {
            // springsecurity 로 찾은 userId 랑 request 로 받은 battle 에서 battle 받은 사람의 userId 비교
            if (userId != battleDao.findOne(request.getBattleId()).getChallenged().getId() &&
                    userId != battleDao.findOne(request.getBattleId()).getChallenger().getId()) {
                return new BaseResponse<>(INVALID_USER_JWT);

            } else if (!"A".equals(battleDao.findOne(request.getBattleId()).getChallenged().getStatus()) &&
                    !"A".equals(battleDao.findOne(request.getBattleId()).getChallenger().getStatus())) {

                return new BaseResponse<>(INVALID_USER);
            } else {
                // 대결 상태 저장
                BattleStatusRes battleStatusRes = battleService.cancel(request);
                return new BaseResponse<>(battleStatusRes);
            }
        } catch (Exception e) {
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
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
            @ApiImplicitParam(name = "request", paramType = "body", value = "문장 작성 Request", dataTypeClass = CreateChallengedSentenceReq.class)
    })
    @ResponseBody
    @PostMapping("/challenged/write")
    public BaseResponse<CreateChallengedSentenceRes> writeSentence(@RequestBody CreateChallengedSentenceReq request) throws BaseException {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String UserId = loggedInUser.getName();

        long userId = Long.parseLong(UserId);

        try {
            // springsecurity 로 찾은 userId 랑 request 로 받은 battle 에서 battle 받은 사람의 userId 비교
            if (userId != battleDao.findOne(request.getBattleId()).getChallenged().getId()) {
                return new BaseResponse<>(INVALID_USER_JWT);
            } else if (!"A".equals(battleDao.findOne(request.getBattleId()).getChallenged().getStatus())) {
                return new BaseResponse<>(INVALID_USER);
            } else {
                // challenged 가 작성한 문장 저장
                CreateChallengedSentenceRes createChallengedSentenceRes = battleService.writeChallenged(userId, request);
                return new BaseResponse<>(createChallengedSentenceRes);
            }
        } catch (BaseException e) {
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }

    }

    /**
     * 대결 신청 API
     * 대결 정보를 받아와서 테이블에 저장
     * [POST] /battles
     */
    @ApiOperation("대결 신청")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
            @ApiImplicitParam(name = "postBattleReq", paramType = "body", value = "대결 신청 body", dataTypeClass = PostBattleReq.class)
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2001, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2004, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 2050, message = "대결 문장을 입력해야 합니다."),
            @ApiResponse(code = 2051, message = "존재하지 않는 키워드입니다."),
            @ApiResponse(code = 2052, message = "이미 대결에 사용한 키워드입니다.")
    })
    @ResponseBody
    @PostMapping("")
    public BaseResponse<Long> createBattle(@RequestBody PostBattleReq postBattleReq) {
        try {
            // 문장이 비어있는지 검사
            if (postBattleReq.getSentence().isBlank()) {
                return new BaseResponse<>(POST_BATTLE_EMPTY_SENTENCE);
            }

            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String user = principal.getUsername();

            Long userIdByAuth = Long.parseLong(user);

            if (!Objects.equals(postBattleReq.getChallengerId(), userIdByAuth)) {
                return new BaseResponse<>(INVALID_JWT);
            }

            Long battleId = battleService.createBattle(postBattleReq);
            return new BaseResponse<>(battleId);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("대결 신청할때 내사진 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", dataTypeClass = String.class, paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "userId", dataTypeClass = Long.class, paramType = "path", value = "유저 인덱스", example = "1")
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
        BattleMyProfilePhotoRes battleMyProfilePhotoRes = new BattleMyProfilePhotoRes(userId, battleService.BattleMyProfilePhoto(userId));
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(battleMyProfilePhotoRes));
    }

    /**
     * 진행 중인 대결 리스트 반환 (최신순)
     * [GET] /battles/list
     *
     * @return 대기 중인 대결 리스트
     */
    @ApiOperation("진행 중인 대결 리스트 반환")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class)
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2001, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2004, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("/list")
    public BaseResponse<List<GetBattlesRes>> getBattles(@RequestParam int page, @RequestParam int limit) {
        try {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String user = principal.getUsername();

            Long userIdByAuth = Long.parseLong(user);

            List<GetBattlesRes> battlesRes = battleProvider.getBattles(userIdByAuth, page, limit);
            return new BaseResponse<>(battlesRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 종료된 대결 리스트 반환 (최신순)
     * [GET] /battles/complete-list
     *
     * @return 종료된 대결 리스트
     */
    @ApiOperation("종료된 대결 리스트 반환")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰")
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2001, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2004, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("/complete-list")
    public BaseResponse<List<GetCompleteBattles>> getCompleteBattles(@RequestParam int page, @RequestParam int limit) {
        try {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String user = principal.getUsername();

            Long userIdByAuth = Long.parseLong(user);

            List<GetCompleteBattles> battlesRes = battleProvider.getCompleteBattles(userIdByAuth, page, limit);
            return new BaseResponse<>(battlesRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 나의 대기 중인 대결 리스트 반환
     * [GET] /battles/wait-list/{userId}
     *
     * @param userId
     * @return
     */
    @ApiOperation("대기 중인 대결 리스트 반환")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
            @ApiImplicitParam(name = "userId", paramType = "path", value = "유저 인덱스", dataTypeClass = Long.class)
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2001, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2004, message = "존재하지 않는 유저입니다.")
    })
    @ResponseBody
    @GetMapping("/wait-list/{userId}")
    public BaseResponse<List<GetWaitBattlesRes>> getWaitBattles(@PathVariable Long userId, @RequestParam int page, @RequestParam int limit) {
        try {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String user = principal.getUsername();

            Long userIdByAuth = Long.parseLong(user);

            if (!Objects.equals(userId, userIdByAuth)) {
                return new BaseResponse<>(INVALID_JWT);
            }

            List<GetWaitBattlesRes> battlesRes = battleProvider.getWaitBattles(userId, page, limit);
            return new BaseResponse<>(battlesRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 나의 진행 중인 대결 리스트 반환 (최신순)
     * [GET] /battles/list/{userId}
     *
     * @return 진행 중인 대결 리스트
     */
    @ApiOperation("나의 진행 중인 대결 리스트 반환")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class)
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2001, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2004, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("/list/{userId}")
    public BaseResponse<List<GetBattlesRes>> getMyBattles(@PathVariable Long userId, @RequestParam int page, @RequestParam int limit) {
        try {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String user = principal.getUsername();

            Long userIdByAuth = Long.parseLong(user);

            if (!userId.equals(userIdByAuth)) {
                return new BaseResponse<>(INVALID_JWT);
            }

            List<GetBattlesRes> battlesRes = battleProvider.getMyBattles(userIdByAuth, page, limit);
            return new BaseResponse<>(battlesRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 나의 종료된 대결 리스트 반환 (최신순)
     * [GET] /battles/complete-list/{userId}
     *
     * @return 종료된 대결 리스트
     */
    @ApiOperation("나의 종료된 대결 리스트 반환")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰")
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2001, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2004, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("/complete-list/{userId}")
    public BaseResponse<List<GetCompleteBattles>> getMyCompleteBattles(@PathVariable Long userId, @RequestParam int page, @RequestParam int limit) {
        try {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String user = principal.getUsername();

            Long userIdByAuth = Long.parseLong(user);

            if (!userId.equals(userIdByAuth)) {
                return new BaseResponse<>(INVALID_JWT);
            }

            List<GetCompleteBattles> battlesRes = battleProvider.getMyCompleteBattles(userId, page, limit);
            return new BaseResponse<>(battlesRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /*
     * 대결 문장 좋아요 API
     * [POST] /battles/like
     */
    @ApiOperation("대결 문장 좋아요 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
            @ApiImplicitParam(name = "request", paramType = "body", value = "문장 좋아요 Request", dataTypeClass = LikeBattleReq.class)
    })
    @ResponseBody
    @PostMapping("/like")
    public BaseResponse<LikeBattleRes> likeBattle(@RequestBody LikeBattleReq request) throws BaseException {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String UserId = loggedInUser.getName();

        long userId = Long.parseLong(UserId);

        try {
            // springsecurity 로 찾은 userId 랑 request 로 받은 sentence 에서 찾은 userId 비교
            if (userId != battleSentenceDao.findOne(request.getSentenceId()).getUser().getId()) {
                return new BaseResponse<>(INVALID_USER_JWT);
            } else if (!"A".equals(battleSentenceDao.findOne(request.getSentenceId()).getUser().getStatus())) {
                return new BaseResponse<>(INVALID_USER);
            } else {
                // 문장 좋아요 저장
                LikeBattleRes likeBattleRes = battleService.like(userId, request);
                return new BaseResponse<>(likeBattleRes);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
        }

    }

    /**
     * 대결 상대 리스트 반환 API
     * 랜덤으로 20명 추출
     * [GET] battles/{userId}/fighters
     */
    @ApiOperation("대결 상대 리스트 반환 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
            @ApiImplicitParam(name = "userId", paramType = "path", value = "유저 인덱스", example = "1", dataTypeClass = Long.class)
    })
    @GetMapping("/{userId}/fighters")
    public BaseResponse<List<BattleFighterRes>> getBattleFighters(@PathVariable Long userId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String springSecurityUserId = principal.getUsername();
        Long userIdx = Long.parseLong(springSecurityUserId);
        try {
            if (userId != userIdx) {
                return new BaseResponse<>(INVALID_JWT);
            } else {
                List<BattleFighterRes> battleFighters = battleProvider.getBattleFighters(userIdx);
                return new BaseResponse<>(battleFighters);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
        }
    }

    /**
     * 대결 정보 반환 API (대기 중, 진행 중)
     * [GET] /battles/run/{battleIdx}
     */
    @ApiOperation("대결 정보 반환 API (대기 중, 진행 중)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰")
    })
    @ResponseBody
    @GetMapping("/run/{battleIdx}")
    public BaseResponse<ReturnRunBattleRes> returnRunBattle(@PathVariable Long battleIdx) throws BaseException {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String UserId = loggedInUser.getName();

        long userId = Long.parseLong(UserId);


        try {
            // springsecurity 로 찾은 userId 랑 request 에서 찾은 userId 비교
            if (userId != battleDao.findOne(battleIdx).getChallenged().getId() &&
                    userId != battleDao.findOne(battleIdx).getChallenger().getId()) {
                return new BaseResponse<>(INVALID_USER_JWT);
            } else if (!"A".equals(userDao.findByUserId(userId).getStatus())) {
                return new BaseResponse<>(INVALID_USER);
            } else {
                // battle 값 return
                ReturnRunBattleRes returnRunBattleRes = battleService.returnRunBattle(battleIdx, userId);
                return new BaseResponse<>(returnRunBattleRes);
            }
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    /**
     * 대결 정보 반환 API (종료)
     * [GET] /battles/finish/{battleIdx}
     */
    @ApiOperation("대결 정보 반환 API (종료)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰")
    })
    @ResponseBody
    @GetMapping("/finish/{battleIdx}")
    public BaseResponse<ReturnFinishBattleRes> returnFinishBattle(@PathVariable Long battleIdx) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String UserId = loggedInUser.getName();

        long userId = Long.parseLong(UserId);


        try {
            // springsecurity 로 찾은 userId 랑 request 에서 찾은 userId 비교
            if (userId != battleDao.findOne(battleIdx).getChallenged().getId() &&
                    userId != battleDao.findOne(battleIdx).getChallenger().getId()) {
                return new BaseResponse<>(INVALID_USER_JWT);
            } else if (!"A".equals(userDao.findByUserId(userId).getStatus())) {
                return new BaseResponse<>(INVALID_USER);
            } else {
                // battle 값 return
                ReturnFinishBattleRes returnFinishBattleRes = battleService.returnFinishBattle(battleIdx, userId);
                return new BaseResponse<>(returnFinishBattleRes);
            }
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    // 대결 키워드 12시 1분마다 3개 넣기
    @Scheduled(cron = "0 1 0 * * ?")
    public void saveBattleWordRandomThree() {
        battleService.saveBattleWordRandomThree();
    }

    /**
     * 대결 키워드 3개 반환 API
     * [GET] battles/{userId}/battleWords
     */
    @ApiOperation("대결 키워드 3개 반환 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
            @ApiImplicitParam(name = "userId", paramType = "path", value = "유저 인덱스", example = "1", dataTypeClass = Long.class)
    })
    @GetMapping("/{userId}/battleWords")
    public BaseResponse<List<GetBattleWordRes>> getBattleWordThree(@PathVariable Long userId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String springSecurityUserId = principal.getUsername();
        Long userIdx = Long.parseLong(springSecurityUserId);
        try {
            if (userId != userIdx) {
                return new BaseResponse<>(INVALID_JWT);
            } else {
                List<GetBattleWordRes> battleWordThree = battleProvider.getBattleWordThree();
                return new BaseResponse<>(battleWordThree);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
        }

    }

}