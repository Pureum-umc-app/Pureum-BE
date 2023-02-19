package com.umc.pureum.domain.mypage;

import com.umc.pureum.domain.mypage.dto.response.GetMySentencesRes;
import com.umc.pureum.domain.mypage.dto.response.MySentenceDto;
import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.repository.SentenceLikeRepository;
import com.umc.pureum.domain.sentence.repository.SentenceRepository;
import com.umc.pureum.global.config.Response.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.umc.pureum.global.config.Response.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MyPageProvider {
    private final SentenceRepository sentenceRepository;
    private final SentenceLikeRepository sentenceLikeRepository;

    // 문장 조회
    public Sentence findSentence(Long sentenceId) throws BaseException {
        Optional<Sentence> sentence = sentenceRepository.findById(sentenceId);
        if (sentence.isPresent()){
            return sentence.get();
        } else {
            throw new BaseException(GET_SENTENCE_EMPTY);
        }
    }

    // 나의 문장 리스트 조회
    public GetMySentencesRes findMySentences(Long userId) {
        List<Sentence> mySentences = sentenceRepository.findByUserIdAndStatusNot(userId,"D");
        List<Sentence> myOpenSentences = sentenceRepository.findByUserIdAndStatus(userId,"O");
        List<MySentenceDto> collect = mySentences.stream().map(s -> MySentenceDto.builder()
                        .sentenceId(s.getId())
                        .word(s.getKeyword().getWord().getWord())
                        .sentence(s.getSentence())
                        .countLike(sentenceLikeRepository.findBySentenceIdAndStatusNot(s.getId(),"D").size())
                        .status(s.getStatus()).build())
                .collect(Collectors.toList());
        return new GetMySentencesRes(mySentences.size(), myOpenSentences.size(), collect);
    }

}
