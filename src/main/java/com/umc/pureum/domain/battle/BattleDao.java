package com.umc.pureum.domain.battle;

import com.umc.pureum.domain.battle.entity.Battle;
import com.umc.pureum.domain.battle.entity.BattleWord;
import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.entity.SentenceLike;
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

    // battle_id 로 battleWord 찾기
//    public Optional<BattleWord> findByBattleWordId(Long battleWordId){
//        List<BattleWord> battleWordList = em.createQuery("select b from BattleWord b where b.battle.id= :battleWordId", BattleWord.class)
//                .setParameter("battleWordId", battleWordId)
//                .getResultList();
//        return battleWordList.stream().findAny();
//    }

    public BattleWord findWord(Long battleId){
        return em.find(BattleWord.class, battleId);
    }
}
