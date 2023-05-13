package com.umc.pureum.domain.blame;

import com.umc.pureum.domain.battle.BattleService;
import com.umc.pureum.domain.battle.entity.BattleSentence;
import com.umc.pureum.domain.battle.repository.BattleRepository;
import com.umc.pureum.domain.blame.entity.BattleSentenceBlame;
import com.umc.pureum.domain.blame.repository.BattleSentenceBlameRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.domain.user.service.UserService;
import com.umc.pureum.global.config.Response.BaseException;
import com.umc.pureum.global.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    private final BattleSentenceBlameRepository battleSentenceBlameRepository;

    @Override
    public void battleSentenceBlame(long userId, long battleSentenceId) throws BaseException {
        Optional<BattleSentenceBlame> battleSentenceBlameOptional = battleSentenceBlameRepository.findByBattleSentenceIdAndUserIdAndStatus(battleSentenceId, userId, BattleSentenceBlame.Status.A);
        UserAccount userAccount = userService.getUser(userId);
        BattleSentence battleSentence = battleService.getBattleSentence(battleSentenceId);
        if (battleSentenceBlameOptional.isPresent()) {
            if(battleSentenceBlameOptional.get().getStatus().equals(BattleSentenceBlame.Status.A))
                battleSentenceBlameOptional.get().updateState(BattleSentenceBlame.Status.D);
            else
                battleSentenceBlameOptional.get().updateState(BattleSentenceBlame.Status.A);
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
        }
        List<BattleSentenceBlame> battleSentenceBlameList =  battleSentenceBlameRepository.findByBattleSentenceIdAndStatus(battleSentenceId,BattleSentenceBlame.Status.A);
        if(battleSentenceBlameList.size()>=10)
            battleSentence.updateStatus(Status.D);
    }
}
