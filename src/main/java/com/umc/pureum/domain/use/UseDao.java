package com.umc.pureum.domain.use;

import com.umc.pureum.domain.use.entity.UsePhone;
import com.umc.pureum.domain.user.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UseDao {
    private final EntityManager em;

    // 사용 테이블 등록
    public void save(UsePhone use){
        em.persist(use);
    }

    // 사용 테이블의 외래키를 통한 최근에 생성된 사용 테이블 단건 조회
    public UsePhone findOneByFk(Long user_id){
        return em.createQuery("select u from UsePhone u where u.user.id = :user_id order by u.createdAt desc limit 1", UsePhone.class)
                .setParameter("user_id",user_id)
                .getSingleResult();
    }

    // 사용 테이블의 외래키를 통한 사용 테이블 모두 조회
    public List<UsePhone> findAll(Long user_id){
        return em.createQuery("select u from UsePhone u where u.user.id = :user_id", UsePhone.class)
                .setParameter("user_id",user_id)
                .getResultList();
    }

 }
