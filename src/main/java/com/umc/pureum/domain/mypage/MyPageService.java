package com.umc.pureum.domain.mypage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyPageService {
    private final MyPageDao myPageDao;
}
