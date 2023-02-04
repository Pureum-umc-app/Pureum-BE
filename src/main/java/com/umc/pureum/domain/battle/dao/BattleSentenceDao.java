package com.umc.pureum.domain.battle.dao;

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

    //대결 문장 저장
    public void save(BattleSentence battleSentence) {
        em.persist(battleSentence);
    }

    //대결 단어 조회
    public BattleWord findByBattleWordId(Long battle_word_id){
        return em.find(BattleWord.class , battle_word_id);
    }

    //대결 문장 조회
    public BattleSentence findOne(Long id){
        return em.find(BattleSentence.class, id);
    }
}
