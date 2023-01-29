package com.umc.pureum.domain.battle;

import com.umc.pureum.domain.battle.dto.PostBattleReq;
import com.umc.pureum.domain.battle.entity.Battle;
import com.umc.pureum.domain.battle.entity.BattleSentence;
import com.umc.pureum.domain.battle.entity.BattleStatus;
import com.umc.pureum.domain.battle.entity.BattleWord;
import com.umc.pureum.domain.battle.repository.BattleRepository;
import com.umc.pureum.domain.battle.repository.BattleSentenceRepository;
import com.umc.pureum.domain.battle.repository.BattleWordRepository;
import com.umc.pureum.domain.sentence.repository.KeywordRepository;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponseStatus;
import com.umc.pureum.global.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BattleService {
    private final BattleDao battleDao;
    private final BattleProvider battleProvider;
    private final BattleRepository battleRepository;
    private final BattleWordRepository battleWordRepository;
    private final BattleSentenceRepository battleSentenceRepository;
    private final UserRepository userRepository;

    /* 대결 신청 API */
    @Transactional
    public Long createBattle(PostBattleReq postBattleReq) throws BaseException {
        // 유저 예외 처리
        Optional<UserAccount> challenger = userRepository.findByIdAndStatus(postBattleReq.getChallengerId(), "A");
        if(challenger.isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }
        Optional<UserAccount> challenged = userRepository.findByIdAndStatus(postBattleReq.getChallengedId(), "A");
        if(challenged.isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }
        // 키워드 예외 처리
        Optional<BattleWord> word = battleWordRepository.findByIdAndStatus(postBattleReq.getWordId(), Status.A);
        if(word.isEmpty()) {
            throw new BaseException(BaseResponseStatus.POST_BATTLE_NO_EXIST_KEYWORD);
        }

        // 대결 저장
        Battle battle = new Battle(challenger.get(), challenged.get(), word.get(), postBattleReq.getDuration(), BattleStatus.W);
        Battle savedBattle = battleRepository.save(battle);

        // 문장 저장
        BattleSentence sentence = new BattleSentence(savedBattle, challenger.get(),
                postBattleReq.getSentence(), word.get(), Status.A);
        battleSentenceRepository.save(sentence);

        return savedBattle.getId();
    }

    public String BattleMyProfilePhoto(long userId) {
        return userRepository.findByIdAndStatus(userId,"A").get().getImage();
    }
}
