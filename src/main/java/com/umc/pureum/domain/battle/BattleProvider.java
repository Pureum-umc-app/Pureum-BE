package com.umc.pureum.domain.battle;

import com.umc.pureum.domain.battle.dto.repsonse.GetWaitBattlesRes;
import com.umc.pureum.domain.battle.entity.Battle;
import com.umc.pureum.domain.battle.entity.BattleStatus;
import com.umc.pureum.domain.battle.repository.BattleRepository;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BattleProvider {
    private final BattleDao battleDao;
    private final BattleRepository battleRepository;
    private final UserRepository userRepository;

    /* 대기 중인 대결 리스트 반환 API */
    public List<GetWaitBattlesRes> getWaitBattles(Long userId) throws BaseException {
        // 유저 예외 처리
        Optional<UserAccount> myInfo = userRepository.findByIdAndStatus(userId, "A");
        if(myInfo.isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }

        return battleRepository.findAllByWaitBattles(userId, BattleStatus.W);
    }
}
