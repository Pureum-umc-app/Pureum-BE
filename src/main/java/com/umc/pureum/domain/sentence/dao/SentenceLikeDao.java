package com.umc.pureum.domain.sentence.dao;

import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.entity.SentenceLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

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


    // sentence_id 로 sentenceLike 찾기
    public Optional<SentenceLike> findBySentenceId(Long sentenceId ,Long userId){
        List<SentenceLike> sentenceLikeList = em.createQuery("select s from SentenceLike s where s.sentence.id= :sentenceId and s.user.id = :userId", SentenceLike.class)
                .setParameter("sentenceId", sentenceId)
                .setParameter("userId", userId)
                .getResultList();
        return sentenceLikeList.stream().findAny();
    }
}
