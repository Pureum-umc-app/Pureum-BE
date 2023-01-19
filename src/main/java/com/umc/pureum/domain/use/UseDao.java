package com.umc.pureum.domain.use;

import com.umc.pureum.domain.use.entity.UsePhone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class UseDao {
    private final EntityManager em;

    // 사용 테이블 단건 조회
    public UsePhone findOne(Long id){
        return em.find(UsePhone.class, id);
    }

    // 사용자의 외래키를 통한 사용 테이블 단건 조회
    public UsePhone findOneByFk(Long user_id){
        return em.createQuery("select u from UsePhone u where u.user.id = :user_id", UsePhone.class)
                .setParameter("user_id",user_id)
                .getSingleResult();
    }
 }
