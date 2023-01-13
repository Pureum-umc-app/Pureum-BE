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

    @GetMapping("/kakao/auth")
    public void getCodeAndToken(@RequestParam String code) throws IOException {
        System.out.println(code);
        kakaoService.getToken(code);
    }

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<String>> SignUp(@RequestBody CreateUserDto createUserDto) throws BaseException {
        String accessToken = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("kakao-ACCESS-TOKEN");
        System.out.println(accessToken);
        try {
            userService.createUser(accessToken, createUserDto);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse("success"));
    }

}
