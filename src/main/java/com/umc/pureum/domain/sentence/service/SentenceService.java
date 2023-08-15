package com.umc.pureum.domain.sentence.service;

import com.umc.pureum.domain.blame.repository.SentenceBlameRepository;
import com.umc.pureum.domain.sentence.dao.SentenceDao;
import com.umc.pureum.domain.sentence.dao.SentenceLikeDao;
import com.umc.pureum.domain.sentence.dto.request.CreateSentenceReq;
import com.umc.pureum.domain.sentence.dto.request.LikeSentenceReq;
import com.umc.pureum.domain.sentence.dto.response.CreateSentenceRes;
import com.umc.pureum.domain.sentence.dto.response.LikeSentenceRes;
import com.umc.pureum.domain.sentence.dto.response.SentenceListRes;
import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.entity.SentenceLike;
import com.umc.pureum.domain.sentence.entity.Word;
import com.umc.pureum.domain.sentence.repository.KeywordRepository;
import com.umc.pureum.domain.sentence.repository.SentenceRepository;
import com.umc.pureum.domain.sentence.repository.WordRepository;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.config.Response.BaseException;
import com.umc.pureum.global.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.umc.pureum.global.config.Response.BaseResponseStatus.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SentenceService {
    private final SentenceRepository sentenceRepository;
    private final SentenceDao sentenceDao;
    private final SentenceLikeDao sentenceLikeDao;
    private final WordRepository wordRepository;
    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;
    private final SentenceBlameRepository sentenceBlameRepository;

    // write : 작성한 문장 DB 에 저장
    @Transactional
    public CreateSentenceRes write(Long userId , CreateSentenceReq request) throws BaseException{
        String writingSentence = request.getSentence();
        Long keywordId = request.getKeywordId();
        String status = request.getStatus();

        // request 로 받은 keywordId 로 단어 찾기
        Keyword keyword = sentenceDao.findByKeywordId(keywordId);
        Word word = keyword.getWord();
        String writingWord = word.getWord();

        // 작성한 문장 존재 여부 확인
        if(Objects.equals(writingSentence, "")){
            throw new BaseException(POST_SENTENCE_EMPTY);
        }

        // 작성할 문장에 단어 포함 여부 확인
        if(!isExist(writingSentence , writingWord)){
            throw new BaseException(POST_SENTENCE_NO_EXISTS_KEYWORD);
        }

        // request 로 받은 userId 로 userAccount 찾기
        UserAccount userAccount = userRepository.findById(userId).get();

        Sentence sentence = new Sentence(userAccount, request.getSentence(), keyword , status);
        sentenceDao.save(sentence);

        return new CreateSentenceRes(sentence.getId());
    }


    /* Sentence 내에 Keyword 존재여부 검사*/
    // isExist : 문장에 키워드가 포함되어있는지 확인하는 함수
    private boolean isExist(String writingSentence , String writingWord) {
        return writingSentence.contains(writingWord);
    }

    // like : 문장 좋아요 DB 에 저장
    @Transactional
    public LikeSentenceRes like(long userId, LikeSentenceReq request) {
        // request 로 받은 sentenceId 로 문장 찾기
        Sentence sentence = sentenceDao.findOne(request.getSentenceId());

        // request 로 받은 userId 로 userAccount 찾기
        UserAccount userAccount = userRepository.findById(userId).get();

        //request 로 받은 sentenceId 로 문장 좋아요 찾기
        if (sentenceLikeDao.findBySentenceId(request.getSentenceId() ,userId).isPresent()) {

            SentenceLike sentenceLike = sentenceLikeDao.findBySentenceId(request.getSentenceId() , userId).get();

            // 존재하는 sentence 일 경우 sentence status 확인하고 status 바꾼다 .
            if (sentenceLike.getStatus() == Status.A) {
                sentenceLike.setStatus(Status.D);
            } else if (sentenceLike.getStatus() == Status.D) {
                sentenceLike.setStatus(Status.A);
            }

            return new LikeSentenceRes(sentenceLike.getId() , sentenceLike.getStatus());

        }

        // 존재하지 않는 sentence 일 경우 sentenceLike 생성해서 저장
        else {
            SentenceLike sentenceLike = new SentenceLike(userAccount, sentence, Status.A);
            sentenceLikeDao.save(sentenceLike);

            return new LikeSentenceRes(sentenceLike.getId() , sentenceLike.getStatus());
        }

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
                Optional<Keyword> keyword = keywordRepository.findByWordIdAndStatus(id, "A");

                if(keyword.isEmpty()) {
                    // Keyword 테이블에 존재하는지 검사 후 없으면 넣기
                    keywordRepository.save(new Keyword(word.get(), "A"));
                    words.add(word.get());
                }
            }
        }
    }

    public List<SentenceListRes> getSentenceList(Long userId, Long wordId, int page, int limit, String sort) {
        List<SentenceListRes> sentenceListRes = new ArrayList<>();
        if(sort.equals("likeCnt")) {
            sentenceListRes = sentenceRepository.findByWordId(wordId, userId, PageRequest.of(page, limit));
            Collections.sort(sentenceListRes, new Comparator<SentenceListRes>() {
                @Override
                public int compare(SentenceListRes o1, SentenceListRes o2) {
                    int fCnt = o1.getLikeCnt();
                    int sCnt = o2.getLikeCnt();

                    if(fCnt < sCnt) return 1;
                    else return -1;
                }
            });

            return sentenceListRes;
        } else if(sort.equals("date")){
            sentenceListRes = sentenceRepository.findByWordId(wordId, userId, PageRequest.of(page, limit));
            return sentenceListRes;
        }

        return sentenceListRes;
    }

    public Sentence getSentence(Long sentenceId) throws BaseException {
        return sentenceRepository.findByIdAndStatus(sentenceId,"A").orElseThrow(() -> new BaseException(NOT_FOUND_SENTENCE));
    }
}

