package com.umc.pureum.domain.user;

import com.umc.pureum.domain.user.service.KakaoService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "유저")
@RequestMapping("/user")
public class UserController {
    private final KakaoService kakaoService;

    @GetMapping("/kakao/auth")
    public void getCodeAndToken(@RequestParam String code) throws IOException {
        System.out.println(code);
        kakaoService.getToken(code);
    }
}
