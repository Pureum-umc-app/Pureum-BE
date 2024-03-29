package com.umc.pureum.domain.mypage;

import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.entity.SentenceLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyPageDao {

    private final EntityManager em;

    // 문장 단건 조회
    public Sentence find(Long id){
        return em.find(Sentence.class, id);
    }


    // 문장 테이블 외래키를 통한 문장 조회
    public List<Sentence> findByFk(Long userId){
        return em.createQuery("select s from Sentence s " +
                        "join fetch s.user u " +
                        "join fetch s.keyword k " +
                        "join fetch k.word w " +
                        "where u.id = :userId and s.status != 'D'", Sentence.class)
                .setParameter("userId",userId)
                .getResultList();
    }

    // 문장 테이블 외래키를 통한 문장 조회(조건 -> 상태가 open 인 경우만 조회)
    public List<Sentence> findByFkOnlyOpen(Long userId){
        return em.createQuery("select s from Sentence s " +
                        "join fetch s.user u " +
                        "join fetch s.keyword k " +
                        "join fetch k.word w " +
                        "where s.user.id = :userId and s.status = 'O'", Sentence.class)
                .setParameter("userId",userId)
                .getResultList();
    }

    // sentence_like 테이블 조회
    public List<SentenceLike> findSentenceLike(Long sentenceId){
        return em.createQuery("select s from SentenceLike s " +
                        "join fetch s.user u " +
                        "join fetch s.sentence ss " +
                        "join fetch ss.keyword k " +
                        "join fetch k.word w " +
                        "where ss.id = :sentenceId and s.status != 'D'")
                .setParameter("sentenceId", sentenceId)
                .getResultList();
    }
}
