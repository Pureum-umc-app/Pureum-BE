package com.umc.pureum.domain.sentence;

import com.umc.pureum.domain.sentence.dto.GetKeywordRes;
import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.repository.KeywordRepository;
import com.umc.pureum.domain.sentence.repository.SentenceRepository;
import com.umc.pureum.domain.use.UseProvider;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SentenceProvider {
    private final KeywordRepository keywordRepository;
    private final SentenceRepository sentenceRepository;
    private final UserRepository userRepository;
    private final UseProvider useProvider;

    /* 오늘의 작성 전 단어 받아오기 */
    public List<GetKeywordRes> getInCompleteKeyword(Long id) throws BaseException {
        if(userRepository.findByIdAndStatus(id, "A").isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }

        // 오늘의 단어 받아오기
        List<Keyword> getWords = keywordRepository.findByCreatedAt();

        // 오늘의 작성 전 단어 받아오기
        List<Keyword> getCompletes = keywordRepository.findCompleteKeyword(id);

        // 결과 리스트
        for (Keyword getComplete : getCompletes) {
            getWords.remove(getComplete);
        }

        return getWords.stream()
                .map(d -> GetKeywordRes.builder()
                        .keywordId(d.getId())
                        .date(new Date(d.getCreatedAt().getTime()) + " " + new Date())
                        .keyword(d.getWord().getWord())
                        .meaning(d.getWord().getMeaning()).build())
                .collect(Collectors.toList());
    }

    /* 오늘의 작성 완료 단어 받아오기 */
    public List<GetKeywordRes> getCompleteKeyword(Long userId) throws BaseException {
        if(userRepository.findByIdAndStatus(userId, "A").isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }

        // 오늘의 작성 완료 단어 받아오기
        List<Keyword> getWords = keywordRepository.findCompleteKeyword(userId);

        return getWords.stream()
                .map(d -> GetKeywordRes.builder()
                        .userId(userId)
                        .keywordId(d.getId())
                        .date(new Date(d.getCreatedAt().getTime()) + " " + new Date())
                        // .date(useProvider.getToday(d.getCreatedAt()) + " " + new Date())
                        .keyword(d.getWord().getWord())
                        .meaning(d.getWord().getMeaning()).build())
                .collect(Collectors.toList());
    }
}
