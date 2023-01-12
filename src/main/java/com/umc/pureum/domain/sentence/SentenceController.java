package com.umc.pureum.domain.sentence;

import com.umc.pureum.domain.home.HomeProvider;
import com.umc.pureum.domain.home.HomeService;
import com.umc.pureum.global.utils.JwtService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "한 문장 챌린지")
@RequestMapping("/sentences")
public class SentenceController {
    private final SentenceProvider sentenceProvider;
    private final SentenceService sentenceService;
    private final JwtService jwtService;

    // @RequiredArgsConstructor 있어서 @Autowired나 생성자는 안 써도 괜찮아요!

    /* 어쩌구 API */
}
