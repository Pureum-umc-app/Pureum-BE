package com.umc.pureum.domain.badge;


import com.umc.pureum.domain.badge.entity.Badge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BadgeDao {

    private final EntityManager em;

    // 배지 저장
    public void save(Badge badge){
        em.persist(badge);
    }

    // 배지 조회
    public Optional<Badge> findBadge(Long userId, int badge){
        List<Badge> badges = em.createQuery("select b from Badge b " +
                        "join fetch b.user u " +
                        "where u.id = :userId and b.badge = :badge", Badge.class)
                .setParameter("userId", userId)
                .setParameter("badge", badge)
                .getResultList();
        return badges.stream().findAny();

    }
}
