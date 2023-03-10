package com.umc.pureum.domain.sentence.service;

import com.umc.pureum.domain.sentence.SentenceDao;
import com.umc.pureum.domain.sentence.SentenceLikeDao;
import com.umc.pureum.domain.sentence.dto.CreateSentenceReq;
import com.umc.pureum.domain.sentence.dto.CreateSentenceRes;
import com.umc.pureum.domain.sentence.dto.LikeSentenceReq;
import com.umc.pureum.domain.sentence.dto.LikeSentenceRes;
import com.umc.pureum.domain.sentence.dto.response.SentenceListRes;
import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.entity.SentenceLike;
import com.umc.pureum.domain.sentence.entity.Word;
import com.umc.pureum.domain.sentence.entity.mapping.SentenceLikeMapping;
import com.umc.pureum.domain.sentence.function.TimeGeneralization;
import com.umc.pureum.domain.sentence.repository.KeywordRepository;
import com.umc.pureum.domain.sentence.repository.SentenceRepository;
import com.umc.pureum.domain.sentence.repository.WordRepository;
import com.umc.pureum.domain.sentence.service.SentenceLikeService;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.config.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.umc.pureum.global.config.BaseResponseStatus.POST_SENTENCE_EMPTY;
import static com.umc.pureum.global.config.BaseResponseStatus.POST_SENTENCE_NO_EXISTS_KEYWORD;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SentenceService {
    private final SentenceLikeService sentenceLikeService;

    private final SentenceRepository sentenceRepository;
    private final SentenceDao sentenceDao;
    private final SentenceLikeDao sentenceLikeDao;
    private final WordRepository wordRepository;
    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;

    // write : ????????? ?????? DB ??? ??????
    @Transactional
    public CreateSentenceRes write(Long userId , CreateSentenceReq request) throws BaseException{

        String writingSentence = request.getSentence();
        Long keywordId = request.getKeywordId();
        String sentenceStatus = request.getStatus();

        // request ??? ?????? keywordId ??? ?????? ??????
        Keyword keyword = sentenceDao.findByKeywordId(keywordId);
        Word word = keyword.getWord();
        String writingWord = word.getWord();

        // ????????? ?????? ?????? ?????? ??????
        if(writingSentence == ""){
            throw new BaseException(POST_SENTENCE_EMPTY);
        }

        // ????????? ????????? ?????? ?????? ?????? ??????
        if(!isExist(writingSentence , writingWord)){
            throw new BaseException(POST_SENTENCE_NO_EXISTS_KEYWORD);
        }



        // request ??? ?????? userId ??? userAccount ??????
        UserAccount userAccount = userRepository.findById(userId).get();

        Sentence sentence = new Sentence(userAccount, request.getSentence(), keyword , sentenceStatus);
        sentenceDao.save(sentence);

        return new CreateSentenceRes(sentence.getId());
    }


    /* Sentence ?????? Keyword ???????????? ??????*/
    // isExist : ????????? ???????????? ????????????????????? ???????????? ??????
    private boolean isExist(String writingSentence , String writingWord) {
        return writingSentence.contains(writingWord);
    }

    // like : ?????? ????????? DB ??? ??????
    @Transactional
    public LikeSentenceRes like(long userId, LikeSentenceReq request) {

        // request ??? ?????? sentenceId ??? ?????? ??????
        Sentence sentence = sentenceDao.findOne(request.getSentenceId());

        // request ??? ?????? userId ??? userAccount ??????
        UserAccount userAccount = userRepository.findById(userId).get();

        //request ??? ?????? sentenceId ??? ?????? ????????? ??????
        if (sentenceLikeDao.findBySentenceId(request.getSentenceId()).isPresent()) {

            SentenceLike sentenceLike = sentenceLikeDao.findBySentenceId(request.getSentenceId()).get();

            // ???????????? sentence ??? ?????? sentence status ???????????? status ????????? .
            if ("A".equals(sentenceLike.getStatus())) {
                sentenceLike.setStatus("D");
            } else if ("D".equals(sentenceLike.getStatus())) {
                sentenceLike.setStatus("A");
            }

            return new LikeSentenceRes(sentenceLike.getId());

        }

        // ???????????? ?????? sentence ??? ?????? sentenceLike ???????????? ??????
        else {
            SentenceLike sentenceLike = new SentenceLike(userAccount, sentence, "A");
            sentenceLikeDao.save(sentenceLike);

            return new LikeSentenceRes(sentenceLike.getId());
        }

    }


    /**
     * ?????? 0?????? ????????? ?????? 3?????? ?????????
     * word?????? ???????????? keyword??? ??????
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void getKeyword() {
        System.out.println("start");
        List<Word> words = new ArrayList<>();

        while(words.size() < 3) {  // ????????? ?????? 3????????? ??????
            // ?????? ?????? ??????
            Random random = new Random();
            Long id = (long) (random.nextInt(4300) + 1);

            // ????????? ?????????
            Optional<Word> word = wordRepository.findById(id);

            if(word.isPresent()) {
                // ????????? ???????????? keyword??? ??????????????? ??????
                Optional<Keyword> keyword = keywordRepository.findByWordIdAndStatus(id, "A");

                if(keyword.isEmpty()) {
                    // Keyword ???????????? ??????????????? ?????? ??? ????????? ??????
                    keywordRepository.save(new Keyword(word.get(), "A"));
                    words.add(word.get());
                }
            }
        }
    }

    public List<SentenceListRes> getSentenceList(long userId, long word_id, int page, int limit, String sort) {
        List<SentenceLikeMapping> sentenceLikeMappings = sentenceLikeService.getSentenceLikeOrderByDate(word_id, page, limit, sort);
        List<SentenceListRes> sentenceListResList = new ArrayList<>();
        TimeGeneralization timeGeneralization = new TimeGeneralization();
        String time;
        SentenceListRes sentenceListRes;
        for (SentenceLikeMapping sentenceLikeMapping : sentenceLikeMappings) {
            time = timeGeneralization.genericTime(sentenceLikeMapping.getTime());
            sentenceListRes = SentenceListRes.builder()
                    .likeNum(sentenceLikeMapping.getLikeNum())
                    .sentenceId(sentenceLikeMapping.getSentence_id())
                    .sentence(sentenceLikeMapping.getSentence())
                    .keywordId(sentenceLikeMapping.getKeywordId())
                    .image(sentenceLikeMapping.getImage())
                    .keyword(sentenceLikeMapping.getKeyword())
                    .nickname(sentenceLikeMapping.getNickname())
                    .userId(sentenceLikeMapping.getUserId())
                    .selfLike(sentenceLikeService.getSentenceSelfLike(userId, sentenceLikeMapping.getSentence_id()))
                    .time(time)
                    .build();
            sentenceListResList.add(sentenceListRes);
        }
        return sentenceListResList;
    }
}

