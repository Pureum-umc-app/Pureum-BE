package com.umc.pureum.domain.battle.dao;

import com.umc.pureum.domain.battle.entity.BattleLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BattleLikeDao {

    private final EntityManager em;

    // 배틀 문장 좋아요 저장
    public void save(BattleLike battleLike){
        em.persist(battleLike);
    }

    // 배틀 문장 좋아요 단건 찾기
    public BattleLike findOne(Long id){
        return em.find(BattleLike.class, id);
    }

    // sentence_id 로 battleLike 찾기
    public Optional<BattleLike> findBySentenceId(Long sentenceId){
        List<BattleLike> battleLikeList = em.createQuery("select b from BattleLike b where b.sentence.id = :sentenceId", BattleLike.class)
                .setParameter("sentenceId", sentenceId)
                .getResultList();
        return battleLikeList.stream().findAny();
    }

}
