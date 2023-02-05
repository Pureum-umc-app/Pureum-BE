package com.umc.pureum.domain.badge;

import com.umc.pureum.domain.badge.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.umc.pureum.domain.badge
 * fileName       : BadgeRepository
 * author         : peter
 * date           : 2023-02-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-02-06        peter       최초 생성
 */
@Repository
@EnableJpaRepositories
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    List<Badge> findByUserId(long userId);
}
