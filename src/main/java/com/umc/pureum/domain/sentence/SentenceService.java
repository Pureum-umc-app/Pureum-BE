package com.umc.pureum.domain.sentence;

import com.umc.pureum.domain.home.HomeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SentenceService {
    private final SentenceDao sentenceDao;
}
