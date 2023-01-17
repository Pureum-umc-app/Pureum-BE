package com.umc.pureum.domain.use;

import com.umc.pureum.domain.use.dto.PostUseTimeAndCountReq;
import com.umc.pureum.domain.use.entity.Use;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class UseDao {
    private final EntityManager em;

    // 사용 테이블 단건 조회
    public Use findOne(Long id){
        return em.find(Use.class, id);
    }

    // 사용자의 외래키를 통한 사용 테이블 단건 조회
    public Use findOneByFk(Long user_id){
        return em.createQuery("select u from Use u where u.user.id = :user_id", Use.class)
                .setParameter("user_id",user_id)
                .getSingleResult();
    }
 }
