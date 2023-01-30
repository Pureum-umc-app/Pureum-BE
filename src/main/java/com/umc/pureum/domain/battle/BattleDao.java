package com.umc.pureum.domain.battle;

import com.umc.pureum.domain.battle.entity.Battle;
import com.umc.pureum.domain.sentence.entity.Sentence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class BattleDao {

    private final EntityManager em;

    //대결 단건 조회
    public Battle findOne(Long id){
        return em.find(Battle.class, id);
    }
}
