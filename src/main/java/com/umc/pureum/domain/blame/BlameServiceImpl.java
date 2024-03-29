package com.umc.pureum.domain.blame;

import com.umc.pureum.domain.battle.BattleService;
import com.umc.pureum.domain.battle.entity.BattleSentence;
import com.umc.pureum.domain.blame.entity.BattleSentenceBlame;
import com.umc.pureum.domain.blame.entity.SentenceBlame;
import com.umc.pureum.domain.blame.repository.BattleSentenceBlameRepository;
import com.umc.pureum.domain.blame.repository.SentenceBlameRepository;
import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.service.SentenceService;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.domain.user.service.UserService;
import com.umc.pureum.global.config.Response.BaseException;
import com.umc.pureum.global.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional
@RequiredArgsConstructor
@Service
public class BlameServiceImpl implements BlameService {
    private final UserService userService;
    private final BattleService battleService;
    private final SentenceService sentenceService;
    private final BattleSentenceBlameRepository battleSentenceBlameRepository;
    private final SentenceBlameRepository sentenceBlameRepository;
    @Override
    public boolean battleSentenceBlame(Long userId, Long battleSentenceId) throws BaseException {
        Optional<BattleSentenceBlame> battleSentenceBlameOptional = battleSentenceBlameRepository.findByBattleSentenceIdAndUserIdAndStatus(battleSentenceId, userId, BattleSentenceBlame.Status.A);
        UserAccount userAccount = userService.getUser(userId);
        BattleSentence battleSentence = battleService.getBattleSentence(battleSentenceId);
        boolean flag;
        if (battleSentenceBlameOptional.isPresent()) {
            if(battleSentenceBlameOptional.get().getStatus().equals(BattleSentenceBlame.Status.A)) {
                battleSentenceBlameOptional.get().updateState(BattleSentenceBlame.Status.D);
                flag = false;
            }
            else {
                battleSentenceBlameOptional.get().updateState(BattleSentenceBlame.Status.A);
                flag = true;
            }
        }
        else {
            BattleSentenceBlame battleSentenceBlame = BattleSentenceBlame.builder()
                    .battleSentence(battleSentence)
                    .user(userAccount)
                    .status(BattleSentenceBlame.Status.A)
                    .build();
            userAccount.addBattleSentenceBlame(battleSentenceBlame);
            battleSentence.addBattleSentenceBlame(battleSentenceBlame);
            battleSentenceBlameRepository.save(battleSentenceBlame);
            flag = true;
        }
        List<BattleSentenceBlame> battleSentenceBlameList =  battleSentenceBlameRepository.findByBattleSentenceIdAndStatus(battleSentenceId,BattleSentenceBlame.Status.A);
        if(battleSentenceBlameList.size()>=10)
            battleSentence.updateStatus(Status.D);
        return flag;
    }

    @Override
    public boolean sentenceBlame(Long userId, Long sentenceId) throws BaseException {
        Optional<SentenceBlame> sentenceBlameOptional = sentenceBlameRepository.findBySentenceIdAndUserIdAndStatus(sentenceId, userId, Status.A);
        UserAccount userAccount = userService.getUser(userId);
        Sentence sentence = sentenceService.getSentence(sentenceId);
        boolean flag;
        if (sentenceBlameOptional.isPresent()) {
            if(sentenceBlameOptional.get().getStatus().equals(Status.A)) {
                sentenceBlameOptional.get().updateState(Status.D);
                flag = false;
            }
            else {
                sentenceBlameOptional.get().updateState(Status.A);
                flag = true;
            }
        }
        else {
            SentenceBlame sentenceBlame = SentenceBlame.builder()
                    .sentence(sentence)
                    .user(userAccount)
                    .status(Status.A)
                    .build();
            userAccount.addSentenceBlame(sentenceBlame);
            sentence.addSentenceBlame(sentenceBlame);
            sentenceBlameRepository.save(sentenceBlame);
            flag = true;
        }
        List<SentenceBlame> sentenceBlameList =  sentenceBlameRepository.findBySentenceIdAndStatus(sentenceId,Status.A);
        if(sentenceBlameList.size()>=10)
            sentence.updateStatus("D");
        return flag;
    }

    @Override
    public boolean getSentenceSelfBlame(Long userId, Long id) {
        return sentenceBlameRepository.findBySentenceIdAndUserIdAndStatus(id,userId,Status.A).isPresent();
    }
}
