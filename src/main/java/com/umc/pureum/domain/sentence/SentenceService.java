package com.umc.pureum.domain.sentence;

import antlr.StringUtils;
import com.umc.pureum.domain.sentence.dto.CreateSentenceReq;
import com.umc.pureum.domain.sentence.dto.CreateSentenceRes;
import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.entity.Word;
import com.umc.pureum.domain.sentence.repository.KeywordRepository;
import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Word;
import com.umc.pureum.domain.sentence.repository.KeywordRepository;
import com.umc.pureum.domain.sentence.repository.WordRepository;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import com.umc.pureum.global.config.BaseResponseStatus;
import com.umc.pureum.global.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import java.util.List;

import static com.umc.pureum.global.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SentenceService {
    private final SentenceDao sentenceDao;
    private final WordRepository wordRepository;
    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;

    // write : 작성한 문장 DB 에 저장
    @Transactional
    public CreateSentenceRes write(CreateSentenceReq request) throws BaseException{

        String writingSentence = request.getSentence();
        Long userId = request.getUserId();
        Long keywordId = request.getKeywordId();
        String sentenceStatus = request.getStatus();

        // request 로 받은 keywordId 로 단어 찾기
        Keyword keyword = sentenceDao.findByKeywordId(keywordId);
        Word word = keyword.getWord();
        String writingWord = word.getWord();

        // 작성한 문장 존재 여부 확인
        if(writingSentence == ""){
            throw new BaseException(POST_SENTENCE_EMPTY);
        }

        // 작성할 문장에 단어 포함 여부 확인
        if(!isExist(writingSentence , writingWord)){
            throw new BaseException(POST_SENTENCE_NO_EXISTS_KEYWORD);
        }



        // request 로 받은 userId 로 userAccount 찾기
        UserAccount userAccount = userRepository.findById(userId).get();

        Sentence sentence = new Sentence(userAccount, request.getSentence(), keyword , sentenceStatus);
        sentenceDao.save(sentence);

        return new CreateSentenceRes(sentence.getId());
    }


    /* Sentence 내에 Keyword 존재여부 검사*/
    // isExist : 문장에 키워드가 포함되어있는지 확인하는 함수
    private boolean isExist(String writingSentence , String writingWord) {
        return writingSentence.contains(writingWord);
    }


    /**
     * 매일 0시에 오늘의 단어 3개를 불러옴
     * word에서 받아와서 keyword에 저장
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void getKeyword() {
        System.out.println("start");
        List<Word> words = new ArrayList<>();

        while(words.size() < 3) {  // 하루에 최대 3개까지 리턴
            // 랜덤 값을 생성
            Random random = new Random();
            Long id = (long) (random.nextInt(4300) + 1);

            // 단어를 받아옴
            Optional<Word> word = wordRepository.findById(id);

            if(word.isPresent()) {
                // 단어가 존재하면 keyword에 존재하는지 검사
                Optional<Keyword> keyword = keywordRepository.findByWordId(id);

                if(keyword.isEmpty()) {
                    // Keyword 테이블에 존재하는지 검사 후 없으면 넣기
                    keywordRepository.save(new Keyword(word.get(), "A"));
                    words.add(word.get());
                }
            }
        }
    }
}
