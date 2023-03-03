package com.umc.pureum.domain.battle.dao;

import com.umc.pureum.domain.battle.entity.Battle;
import com.umc.pureum.domain.battle.entity.BattleWord;
import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

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


    // battleWord 저장
    public void saveBattleWord(BattleWord battleWord){
        em.persist(battleWord);
    }

    // battleWord 테이블 가져오기
    public List<BattleWord> getBattleWordTable(){
        return em.createQuery("select b from BattleWord b",BattleWord.class)
                .getResultList();
    }

    // keyword 테이블 가져오기
    public List<Keyword> getKeywordTable(){
        return em.createQuery("select k from Keyword k order by k.createdAt desc ", Keyword.class)
                .setMaxResults(3)
                .getResultList();
    }

    // 최근 keyword, 전 battleWord 와 안 겹치게 단어 랜덤 3개 추출
    public List<Word> getRandomThreeBattleWord(List<Long> wordId){
        return em.createQuery("select w from Word w where w.id not in (:wordId) order by RAND()", Word.class)
                .setParameter("wordId", wordId)
                .setMaxResults(3)
                .getResultList();
    }

    // BattleWord 에서 최근 생성된 3개 추출
    public List<BattleWord> getBattleWordThreeRecently(){
        return em.createQuery("select b from BattleWord b " +
                        "join fetch b.word w " +
                        "order by b.createdAt desc", BattleWord.class)
                .setMaxResults(3)
                .getResultList();
    }



}
