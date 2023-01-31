package com.umc.pureum.domain.battle;

import com.umc.pureum.domain.battle.dto.BattleStatusReq;
import com.umc.pureum.domain.battle.dto.BattleStatusRes;
import com.umc.pureum.domain.battle.dto.CreateChallengedSentenceReq;
import com.umc.pureum.domain.battle.dto.CreateChallengedSentenceRes;
import com.umc.pureum.domain.battle.entity.Battle;
import com.umc.pureum.domain.battle.entity.BattleSentence;
import com.umc.pureum.domain.battle.entity.BattleStatus;
import com.umc.pureum.domain.battle.entity.BattleWord;
import com.umc.pureum.domain.sentence.dto.CreateSentenceReq;
import com.umc.pureum.domain.sentence.dto.CreateSentenceRes;
import com.umc.pureum.domain.sentence.dto.LikeSentenceReq;
import com.umc.pureum.domain.sentence.dto.LikeSentenceRes;
import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.entity.SentenceLike;
import com.umc.pureum.domain.sentence.entity.Word;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.umc.pureum.global.config.BaseResponseStatus.POST_SENTENCE_EMPTY;
import static com.umc.pureum.global.config.BaseResponseStatus.POST_SENTENCE_NO_EXISTS_KEYWORD;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BattleService {
    private final BattleDao battleDao;
    private final BattleSentenceDao battleSentenceDao;
    private final UserRepository userRepository;
    private final BattleProvider battleProvider;

    // accept : 대결 수락
    @Transactional
    public BattleStatusRes accept(BattleStatusReq request) {

        // request 로 받은 battleId 로 battle 찾기
        Battle battle = battleDao.findOne(request.getBattleId());

        // battle 상태를 수락 완료로 바꾸기
        battle.setStatus(BattleStatus.A);

        // battle ID , Status 값
        return new BattleStatusRes(battle.getId(), battle.getStatus());
    }

    // reject : 대결 거절
    @Transactional
    public BattleStatusRes reject(BattleStatusReq request) {

        // request 로 받은 battleId 로 battle 찾기
        Battle battle = battleDao.findOne(request.getBattleId());

        // battle 상태를 수락 완료로 바꾸기
        battle.setStatus(BattleStatus.D);

        // battle ID , Status 값
        return new BattleStatusRes(battle.getId(), battle.getStatus());
    }

    // cancel : 대결 취소
    @Transactional
    public BattleStatusRes cancel(BattleStatusReq request) {

        // request 로 받은 battleId 로 battle 찾기
        Battle battle = battleDao.findOne(request.getBattleId());

        // battle 상태를 수락 완료로 바꾸기
        battle.setStatus(BattleStatus.D);

        // battle ID , Status 값
        return new BattleStatusRes(battle.getId(), battle.getStatus());
    }

    // writeChallenged : challenged 의 문장 작성 DB 에 저장
    @Transactional
    public CreateChallengedSentenceRes writeChallenged(Long userId , CreateChallengedSentenceReq request) throws BaseException {

        Long battleId = request.getBattleId();
        String writingSentence = request.getSentence();

        // request 로 받은 battleId 로 배틀 찾기
        Battle battle = battleDao.findOne(battleId);

        // battle 상태 바꾸기
       battle.setStatus(BattleStatus.I);

        // request 로 받은 battleWordId 로 단어 찾기
        BattleWord battleWord = battle.getWord();
        Word word = battleWord.getWord();
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

        BattleSentence battleSentence = new BattleSentence(battle , userAccount , writingSentence , battleWord , Status.A);
        battleSentenceDao.save(battleSentence);

        return new CreateChallengedSentenceRes(battleSentence.getId() , battle.getId() , battle.getStatus());
    }


    /* Sentence 내에 Keyword 존재여부 검사*/
    // isExist : 문장에 키워드가 포함되어있는지 확인하는 함수
    private boolean isExist(String writingSentence , String writingWord) {
        return writingSentence.contains(writingWord);
    }
}
