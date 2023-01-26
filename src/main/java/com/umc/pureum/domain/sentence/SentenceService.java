package com.umc.pureum.domain.sentence;

import antlr.StringUtils;
import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.entity.Word;
import com.umc.pureum.domain.sentence.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SentenceService {
    private final SentenceDao sentenceDao;

    // write : 작성한 문장 DB 에 저장
    public Long write(Sentence sentence) {

        String writingSentence = sentence.getSentence();

        Keyword keyword = sentence.getKeyword();
        Word word = keyword.getWord();
        String writingWord =  word.getWord();

        if(!isExist(writingSentence , writingWord)){
            throw new IllegalStateException("키워드가 포함되어있지 않습니다");
        }

        sentenceDao.save(sentence);
        return sentence.getId();
    }

    /* Sentence 내에 Keyword 존재여부 검사*/
    // isExist : 문장에 키워드가 포함되어있는지 확인하는 함수
    private boolean isExist(String writingSentence , String writingWord) {
        return writingSentence.contains(writingWord);
    }


}
