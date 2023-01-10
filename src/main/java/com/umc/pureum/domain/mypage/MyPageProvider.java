package com.umc.pureum.domain.mypage;

import com.umc.pureum.global.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyPageProvider {
    private final MyPageDao myPageDao;
    private final JwtService jwtService;
}
