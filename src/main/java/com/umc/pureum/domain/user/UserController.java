package com.umc.pureum.domain.user;

import com.umc.pureum.global.utils.JwtService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "회원가입 및 로그인")
@RequestMapping("/users")
public class UserController {
    private final UserProvider userProvider;
    private final UserService userService;
    private final JwtService jwtService;

    // @RequiredArgsConstructor 있어서 @Autowired나 생성자는 안 써도 괜찮아요!

    /* 어쩌구 API */
}
