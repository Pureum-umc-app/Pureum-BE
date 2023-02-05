package com.umc.pureum.domain.badge;


import com.umc.pureum.domain.badge.entity.Badge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class BadgeDao {

    private final EntityManager em;

    // 배지 저장
    public void save(Badge badge){
        em.persist(badge);
    }

    // 배지 조회
    public Badge find(Long userId, int badge){
        return em.createQuery("select b from Badge b where b.userAccount.id = :userId and b.badge = :badge", Badge.class)
                .setParameter("userId", userId)
                .setParameter("badge", badge)
                .getSingleResult();
    }
}
