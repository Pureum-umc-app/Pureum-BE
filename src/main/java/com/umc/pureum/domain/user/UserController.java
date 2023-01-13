package com.umc.pureum.domain.user;

import com.umc.pureum.domain.user.dto.request.CreateUserDto;
import com.umc.pureum.domain.user.service.KakaoService;
import com.umc.pureum.domain.user.service.UserService;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

import static com.umc.pureum.global.config.BaseResponseStatus.DATABASE_ERROR;

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
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<String>> SignUp(@RequestBody CreateUserDto createUserDto) throws BaseException {
        String accessToken = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("kakao-ACCESS-TOKEN");
        try {
            userService.createUser(accessToken, createUserDto);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse("success"));
    }

}
