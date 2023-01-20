package com.umc.pureum.domain.sentence;

import com.umc.pureum.global.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SentenceProvider {
    private final SentenceDao sentenceDao;
    private final JwtService jwtService;
}
