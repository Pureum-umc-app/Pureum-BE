package com.umc.pureum.domain.badge;

import com.umc.pureum.domain.badge.dto.response.GetBadgeInfoRes;
import com.umc.pureum.domain.badge.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@EnableJpaRepositories
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    List<Badge> findByUserId(long id);

    @Query("select b.badge as badge, b.createdAt as createdAt \n" +
            "from Badge as b \n" +
            "where b.user.id = :userId")
    List<GetBadgeInfoRes> findBadgesByUserId(@Param("userId") Long userId);
}
