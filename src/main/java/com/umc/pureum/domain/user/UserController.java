package com.umc.pureum.domain.user;

import com.sun.net.httpserver.Authenticator;
import com.umc.pureum.domain.user.dto.AccessTokenInfoDto;
import com.umc.pureum.domain.user.dto.request.CreateUserDto;
import com.umc.pureum.domain.user.service.KakaoService;
import com.umc.pureum.domain.user.service.UserService;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import com.umc.pureum.global.config.BaseResponseStatus.*;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
            @ApiImplicitParam(name = "nickname",dataType = "String",value = "닉네임"),
            @ApiImplicitParam(name = "grade",dataType = "int",value = "학년"),
            @ApiImplicitParam(name = "image",dataType = "imageFile",value = "프로필 이미지")
    })
    @ApiResponses({
            @ApiResponse(code = 1000,message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2031,message = "중복된 닉네임입니다."),
            @ApiResponse(code = 2033,message = "이미 가입된 회원입니다.")
    })
    @Transactional
    @CrossOrigin
    @PostMapping(value="/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<String>> SignUp(HttpServletRequest request, @RequestParam(value="image") MultipartFile image, CreateUserDto createUserDto) throws BaseException {
        createUserDto.setProfile_photo(image);
        String accessToken = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("kakao-ACCESS-TOKEN");
        try {
            if (userService.validationDuplicateUserNickname(createUserDto.getNickname())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse(POST_USERS_EXISTS_NICKNAME));
            }
            //accessToken로 user 정보 가져오기
            AccessTokenInfoDto accessTokenInfoDto = kakaoService.getUserInfoByKakaoToken(accessToken);
            if (userService.validationDuplicateKakaoId(accessTokenInfoDto.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse(POST_USERS_EXISTS));
            }
            userService.createUser(accessTokenInfoDto, createUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse("회원가입완료"));
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}