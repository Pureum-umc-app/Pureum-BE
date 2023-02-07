package com.umc.pureum.domain.mypage;

import com.umc.pureum.domain.mypage.dto.response.GetMySentencesRes;
import com.umc.pureum.domain.mypage.dto.response.MySentenceDto;
import com.umc.pureum.domain.sentence.entity.Sentence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MyPageProvider {
    private final MyPageDao myPageDao;

    // 문장 조회
    public Sentence findSentence(Long sentenceId){
        return myPageDao.find(sentenceId);
    }

    // 나의 문장 리스트 조회
    public GetMySentencesRes findMySentences(Long userId){
        List<Sentence> mySentences = myPageDao.findByFk(userId);
        List<Sentence> myOpenSentences = myPageDao.findByFkOnlyOpen(userId);
        List<MySentenceDto> collect = mySentences.stream().map(s -> MySentenceDto.builder()
                        .sentenceId(s.getId())
                        .word(s.getKeyword().getWord().getWord())
                        .sentence(s.getSentence())
                        .countLike(myPageDao.findSentenceLike(s.getId()).size())
                        .status(s.getStatus()).build())
                .collect(Collectors.toList());
        return new GetMySentencesRes(mySentences.size(), myOpenSentences.size(), collect);
    }

}
