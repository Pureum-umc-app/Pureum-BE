package com.umc.pureum.domain.mypage;

import com.umc.pureum.global.utils.JwtService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "마이페이지")
@RequestMapping("/mypages")
public class MyPageController {
    private final MyPageProvider myPageProvider;
    private final MyPageService myPageService;
    private final JwtService jwtService;

    // @RequiredArgsConstructor 있어서 @Autowired나 생성자는 안 써도 괜찮아요!

    /* 어쩌구 API */
}
