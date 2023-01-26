package com.umc.pureum.domain.user;

import com.umc.pureum.domain.user.dto.KakaoAccessTokenInfoDto;
import com.umc.pureum.domain.user.dto.request.CreateUserDto;
import com.umc.pureum.domain.user.dto.response.GetProfileResponseDto;
import com.umc.pureum.domain.user.dto.response.LogInResponseDto;
import com.umc.pureum.domain.user.service.KakaoService;
import com.umc.pureum.domain.user.service.UserService;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;

import static com.umc.pureum.global.config.BaseResponseStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "유저")
@RequestMapping("/user")

public class UserController {
    private final KakaoService kakaoService;
    private final UserService userService;

    /**
     * 서버에서만 사용할 API
     * 인가코드로 access token, refresh token 발급
     *
     * @param code // 인가코드
     * @throws IOException // 카카오 서버 접속 오류 예외처리
     */
    // kauth.kakao.com/oauth/authorize?client_id=633bdb4f088357e5fe5cde61b4543053&redirect_uri=http://localhost:9000/user/kakao/auth&response_type=code
    //위의 링크로 접속하면 console 창에 토큰 정보 나오는데 그거 사용하면 됩니다.
    @ApiIgnore
    @ApiOperation("(서버전용)인가 코드로 토큰 받아오는 API ")
    @GetMapping("/kakao/auth")
    public void getCodeAndToken(@RequestParam String code) throws IOException {
        System.out.println(code);
        kakaoService.getToken(code);
    }

    /**
     * 회원가입 API
     *
     * @param createUserDto // 유저 생성 DTO
     * @return // 회원가입 성공시 success 출력
     * @throws BaseException // DB 에러 등등
     */
    @ApiOperation("회원가입 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name="kakao-ACCESS-TOKEN", paramType = "header",value = "kakao-ACCESS-TOKEN"),
            @ApiImplicitParam(name = "nickname", paramType = "formData", value = "nickname"),
            @ApiImplicitParam(name = "grade", paramType = "formData", value = "grade"),
            @ApiImplicitParam(name = "image", paramType = "formData", value = "image")
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2031, message = "중복된 닉네임입니다."),
            @ApiResponse(code = 2033, message = "이미 가입된 회원입니다.")
    })
    @CrossOrigin
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<String>> SignUp(@RequestParam(value = "image", required = false) MultipartFile image, CreateUserDto createUserDto) throws BaseException {
        System.out.println("image= ");
        if (!image.isEmpty()) createUserDto.setImage(image);
        else createUserDto.setImage(null);
        String accessToken = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("kakao-ACCESS-TOKEN");
        try {
            if (userService.validationDuplicateUserNickname(createUserDto.getNickname())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse<>(POST_USERS_EXISTS_NICKNAME));
            }
            //accessToken로 user 정보 가져오기
            KakaoAccessTokenInfoDto kakaoAccessTokenInfoDto = kakaoService.getUserInfoByKakaoToken(accessToken);
            if (userService.validationDuplicateKakaoId(kakaoAccessTokenInfoDto.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse<>(POST_USERS_EXISTS));
            }
            System.out.println(kakaoAccessTokenInfoDto+"\n"+createUserDto);
            userService.createUser(kakaoAccessTokenInfoDto, createUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>("회원가입완료"));
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 로그인 API
     *
     * @return jwt token
     */
    @ApiOperation("로그인 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kakao-ACCESS-TOKEN", paramType = "header", value = "kakao-access token"),
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다.", response = LogInResponseDto.class),
            @ApiResponse(code = 2022, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2034, message = "존재하지 않는 회원입니다.")
    })
    @PostMapping(value = "/signin")
    public ResponseEntity<BaseResponse<LogInResponseDto>> userLogIn() {
        String accessToken = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("kakao-ACCESS-TOKEN");
        KakaoAccessTokenInfoDto kakaoAccessTokenInfoDto = kakaoService.getUserInfoByKakaoToken(accessToken);
        if (!userService.validationDuplicateKakaoId(kakaoAccessTokenInfoDto.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse<>(POST_USERS_NO_EXISTS_USER));
        }
        Long id = userService.getUserId(kakaoAccessTokenInfoDto.getId());
        LogInResponseDto logInResponseDto = userService.userLogIn(id);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(logInResponseDto));
    }

    @ApiOperation("닉네임 유효성 체크 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", paramType = "path", value = "닉네임"),
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다.", response = String.class),
            @ApiResponse(code = 2031, message = "중복된 닉네임입니다."),
    })
    @GetMapping(value = "/nickname/{nickname}/validation")
    public ResponseEntity<BaseResponse<String>> ValidationUserNickName(@PathVariable String nickname) throws BaseException {
        try {
            if (userService.validationDuplicateUserNickname(nickname)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse<>(POST_USERS_EXISTS_NICKNAME));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>("유효한 닉네임입니다."));
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @ApiOperation("프로필 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰"),
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다.", response = GetProfileResponseDto.class),
    })
    @GetMapping(value = "/profile")
    public ResponseEntity<BaseResponse<GetProfileResponseDto>> GetProfile() throws BaseException {
        try {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long id = Long.parseLong(principal.getUsername());
            GetProfileResponseDto getProfileResponseDto = userService.GetProfile(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(getProfileResponseDto));
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}