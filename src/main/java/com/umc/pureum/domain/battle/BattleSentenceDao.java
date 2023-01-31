package com.umc.pureum.domain.battle;

import com.umc.pureum.domain.battle.entity.BattleSentence;
import com.umc.pureum.domain.battle.entity.BattleWord;
import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.entity.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class BattleSentenceDao {

    private final EntityManager em;

    //battleSentence 저장
    public void save(BattleSentence battleSentence) {
        em.persist(battleSentence);
    }

    //battleWord 조회
    public BattleWord findByBattleWordId(Long battle_word_id){
        return em.find(BattleWord.class , battle_word_id);
    }

}
