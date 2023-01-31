package com.umc.pureum.domain.use;


import com.umc.pureum.domain.mypage.dto.GetMySentencesRes;
import com.umc.pureum.domain.sentence.dto.CreateSentenceReq;
import com.umc.pureum.domain.sentence.dto.CreateSentenceRes;
import com.umc.pureum.domain.sentence.dto.LikeSentenceRes;
import com.umc.pureum.domain.use.dto.GetGoalResultsRes;
import com.umc.pureum.domain.use.dto.GetHomeListRes;
import com.umc.pureum.domain.use.dto.PostUseTimeAndCountReq;
import com.umc.pureum.domain.use.dto.PostUseTimeAndCountRes;
import com.umc.pureum.domain.use.dto.rank.RankInformationDto;
import com.umc.pureum.domain.use.dto.rank.RankerInformationDto;
import com.umc.pureum.domain.use.dto.request.ReturnGradeReq;
import com.umc.pureum.domain.use.dto.request.ReturnGradeRes;
import com.umc.pureum.domain.use.dto.request.SetUsageTimeReq;
import com.umc.pureum.domain.user.UserDao;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
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
    private final UserDao userDao;

    /**
     * 일일 사용 시간, 휴대폰 켠 횟수 저장 API
     * [POST] /uses/{user_id}/useTimeAndCount
     */
    @ApiOperation("일일 사용 시간, 휴대폰 켠 횟수 저장")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "user_idx", paramType = "path", value = "유저 인덱스", example = "1"),
            @ApiImplicitParam(name = "hour", paramType = "formData", value = "일일 사용 시간(시)"),
            @ApiImplicitParam(name = "minute", paramType = "formData", value = "일일 사용 시간(분)"),
            @ApiImplicitParam(name = "count", paramType = "formData", value = "휴대폰 켠 횟수"),
    })
    @ResponseBody
    @PostMapping("/{user_idx}/useTimeAndCount")
    public BaseResponse<PostUseTimeAndCountRes> saveUseTimeAndCount(@PathVariable Long user_idx, @RequestBody PostUseTimeAndCountReq postUseTimeAndCountReq) {
        // springSecurity 에서 userId 받아와서 Long 형으로 바꿈
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String springSecurityUserId = principal.getUsername();
        Long userId = Long.parseLong(springSecurityUserId);
        try {
            if (userId != user_idx) {
                return new BaseResponse<>(INVALID_JWT);
            } else {
                PostUseTimeAndCountRes postUseTimeAndCountRes = useService.saveTimeAndCount(userId, postUseTimeAndCountReq);
                return new BaseResponse<>(postUseTimeAndCountRes);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
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
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰")
    })
    @ResponseBody
    @GetMapping("/{userId}/goals/result")
    public BaseResponse<GetGoalResultsRes> getGoalResults(@PathVariable Long userId) {
        try {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String user = principal.getUsername();

            Long userIdByAuth = Long.parseLong(user);

            if (!Objects.equals(userId, userIdByAuth)) {
                return new BaseResponse<>(INVALID_JWT);
            } else {
                GetGoalResultsRes getGoalResultsRes = useProvider.getGoalResults(userId);
                return new BaseResponse<>(getGoalResultsRes);
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("목표 사용 시간 설정 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "userId", paramType = "path", value = "유저 인덱스", example = "1"),
            @ApiImplicitParam(name = "SetUsageTimeReq", paramType = "body", value = "목표사용시간")
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
        if (id != userId)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponse<>(INVALID_JWT));
        if (useService.existUsageTime(userId))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>(POST_USE_EXISTS_USAGE_TIME));
        useService.setUsageTime(userId, setUsageTimeReq);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>("설정하다"));
    }

    /**
     * 홈 화면 리스트 반환 API
     * [GET] uses/{userIdx}
     */
    @ApiOperation("홈 화면 리스트 반환 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "userIdx", paramType = "path", value = "유저 인덱스", example = "1"),
    })
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetHomeListRes>> getHomeList(@PathVariable Long userIdx) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String springSecurityUserId = principal.getUsername();
        Long userId = Long.parseLong(springSecurityUserId);
        try {
            if (userId != userIdx) {
                return new BaseResponse<>(INVALID_JWT);
            } else {
                List<GetHomeListRes> homeListRes = useProvider.getHomeListRes(userId);
                return new BaseResponse<>(homeListRes);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
        }

    }

    /**
     * 나의 학년 카테고리 반환 API
     * [GET] /{userId}/uses/grade
     */
    @ApiOperation("나의 학년 카테고리 반환 API ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰")
    })
    @ResponseBody
    @GetMapping("/{userId}/grade")
    public BaseResponse<ReturnGradeRes> myGrade(@PathVariable Long userId) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String UserId = loggedInUser.getName();

        long id = Long.parseLong(UserId);

        try{
            // springsecurity 로 찾은 userId 랑 request 에서 찾은 userId 비교
            if(id != userId){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            else{
                // user 의 grade 찾기
                ReturnGradeRes returnGradeRes = useService.returnGrade(userId);
                return new BaseResponse<>(returnGradeRes);

            }
        }catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
        }

    }

    /**
     * 날짜 별 랭킹(같은 카테고리(학년) 내) 조회 API
     * 그 랭킹에서 자신의 랭킹 정보 또한 조회
     * [GET] /uses/rankInSameGrade
     * 페이징 처리함!(25개 씩)
     * 파라미터 인자로 날짜( ex)"2023-01-31" ), 페이지를 받음.
     */
    @GetMapping("/rankInSameGrade")
    @ApiOperation("날짜 별 랭킹(같은 카테고리(학년) 내) 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "date", paramType = "query", value = "날짜", example = "2023-01-18", dataType = "string"),
            @ApiImplicitParam(name = "date", paramType = "query", value = "페이지", example = "0", dataType = "int"),
    })
    public BaseResponse<RankInformationDto> getRankInSameGrade(@RequestParam String date, @RequestParam int page) throws BaseException{
        try{
            // springSecurity 에서 userId 받아와서 Long 형으로 바꿈
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String springSecurityUserId = principal.getUsername();
            Long userId = Long.parseLong(springSecurityUserId);

            // 날짜 별 랭킹(같은 카테고리(학년) 내) 조회
            List<RankerInformationDto> rankerInformationByDateInSameGrade = useProvider.getRankerInformationByDateInSameGrade(userId, date, page);

            RankInformationDto rankInformationDto = getRankInformationDto(userId, rankerInformationByDateInSameGrade);

            return new BaseResponse<>(rankInformationDto);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 날짜 별 랭킹(전체) 조회 API
     * 그 랭킹에서 자신의 랭킹 정보 또한 조회
     * [GET] /uses/rankInAllGrade
     * 페이징 처리함!(25개 씩)
     * 파라미터 인자로 날짜( ex)"2023-01-31" ), 페이지를 받음.
     */
    @GetMapping("/rankInAllGrade")
    @ApiOperation("날짜 별 랭킹(같은 카테고리(학년) 내) 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "date", paramType = "query", value = "날짜", example = "2023-01-18", dataType = "string"),
            @ApiImplicitParam(name = "date", paramType = "query", value = "페이지", example = "0", dataType = "int"),
    })
    public BaseResponse<RankInformationDto> getRankInAllGrade(@RequestParam String date, @RequestParam int page) throws BaseException{
        try{
            // springSecurity 에서 userId 받아와서 Long 형으로 바꿈
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String springSecurityUserId = principal.getUsername();
            Long userId = Long.parseLong(springSecurityUserId);

            // 날짜 별 랭킹(전체) 조회
            List<RankerInformationDto> rankerInformationByDateInAllGrade = useProvider.getRankerInformationByDateInAllGrade(date, page);

            RankInformationDto rankInformationDto = getRankInformationDto(userId, rankerInformationByDateInAllGrade);

            return new BaseResponse<>(rankInformationDto);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 랭킹에서 사용자 정보 빼오기
    private RankInformationDto getRankInformationDto(Long userId, List<RankerInformationDto> rankerInformationDtos) {

        // 자기 순위(with 몇몇 정보) 찾기
        RankerInformationDto myRankInformation = rankerInformationDtos.stream()
                .filter(r -> userDao.find(userId).getNickname().equals(r.getNickname()))
                .findAny()
                .orElse(null);

        // RankInformationDto 에 담기
        RankInformationDto rankInformationDto = RankInformationDto.builder()
                .myRank(myRankInformation)
                .allRank(rankerInformationDtos).build();

        return rankInformationDto;
    }

}
