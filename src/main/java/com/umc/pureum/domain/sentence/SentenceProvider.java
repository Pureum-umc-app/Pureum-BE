package com.umc.pureum.domain.sentence;

import com.umc.pureum.domain.sentence.dto.GetBeforeKeywordRes;
import com.umc.pureum.global.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SentenceProvider {
    private final SentenceDao sentenceDao;
    private final JwtService jwtService;

    public List<GetBeforeKeywordRes> getBeforeKeyword(Long id) {
        List<GetBeforeKeywordRes> getBeforeKeywordRes = new ArrayList<>();

        return getBeforeKeywordRes;
    }
}
