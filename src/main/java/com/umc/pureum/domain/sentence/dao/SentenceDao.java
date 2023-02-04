package com.umc.pureum.domain.sentence.dao;

import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Sentence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SentenceDao {

    private final EntityManager em;

    //문장 저장
    public void save(Sentence sentence) {
        Long word_id = sentence.getKeyword().getWord().getId();
        em.persist(sentence);
    }

    //문장 단건 조회
    public Sentence findOne(Long id){
        return em.find(Sentence.class, id);
    }

    //문장 모두 조회
    public List<Sentence> findAll(){
        return em.createQuery("select s from Sentence s ", Sentence.class)
                .getResultList();
    }

    //keyword 조회
    public Keyword findByKeywordId(Long word_id){
        return em.find(Keyword.class , word_id);
    }

}
