package com.umc.pureum.domain.battle;

import com.umc.pureum.domain.battle.dto.repsonse.GetBattleInfoRes;
import com.umc.pureum.domain.battle.dto.repsonse.GetWaitBattlesRes;
import com.umc.pureum.domain.battle.entity.Battle;
import com.umc.pureum.domain.battle.entity.BattleWord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BattleDao {

    private final EntityManager em;

    //대결 단건 조회
    public Battle findOne(Long battleId){
        return em.find(Battle.class, battleId);
    }

    //대결 단어 조회
    public BattleWord findWord(Long battleId){
        return em.find(BattleWord.class, battleId);
    }


}
