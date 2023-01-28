package com.umc.pureum.domain.sentence;

import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.entity.SentenceLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class SentenceLikeDao {

    private final EntityManager em;

    // 문장 좋아요 저장
    public void save(SentenceLike SentenceLike) {
        em.persist(SentenceLike);
    }

    //문장 좋아요 단건 조회
    public SentenceLike findOne(Long id){
        return em.find(SentenceLike.class, id);
    }
}
