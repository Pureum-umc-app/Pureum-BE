package com.umc.pureum.domain.sentence;

import com.umc.pureum.domain.sentence.dto.response.GetKeywordRes;
import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.repository.KeywordRepository;
import com.umc.pureum.domain.use.UseProvider;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.global.config.Response.BaseException;
import com.umc.pureum.global.config.Response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SentenceProvider {
    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;
    private final UseProvider useProvider;

    /* 오늘의 단어 받아오기 */
    public List<GetKeywordRes> getKeyword(Long userId) throws BaseException {
        // 유저 검사
        if(userRepository.findByIdAndStatus(userId, "A").isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }

        // 오늘의 단어 & 작성 전 단어 받아오기
        PageRequest request = PageRequest.of(0, 3);
        List<Keyword> getWords = keywordRepository.findByCreatedAt(request);

        return getWords.stream()
                .map(d -> GetKeywordRes.builder()
                        .userId(userId)
                        .keywordId(d.getId())
                        .date(useProvider.getToday(d.getCreatedAt()))
                        .keyword(d.getWord().getWord())
                        .meaning(d.getWord().getMeaning()).build())
                .collect(Collectors.toList());
    }

    /* 오늘의 작성 전 단어 받아오기 */
    public List<GetKeywordRes> getInCompleteKeyword(Long userId) throws BaseException {
        // 유저 검사
        if(userRepository.findByIdAndStatus(userId, "A").isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }

        // 오늘의 단어 & 작성 전 단어 받아오기
        PageRequest request = PageRequest.of(0, 3);
        List<Keyword> getWords = keywordRepository.findByCreatedAt(request);
        List<Keyword> getCompletes = keywordRepository.findCompleteKeyword(userId);

        // 결과 리스트
        for (Keyword getComplete : getCompletes) {
            getWords.remove(getComplete);
        }

        return getWords.stream()
                .map(d -> GetKeywordRes.builder()
                        .userId(userId)
                        .keywordId(d.getId())
                        .date(useProvider.getToday(d.getCreatedAt()))
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
                        .date(useProvider.getToday(d.getCreatedAt()))
                        .keyword(d.getWord().getWord())
                        .meaning(d.getWord().getMeaning()).build())
                .collect(Collectors.toList());
    }
}
