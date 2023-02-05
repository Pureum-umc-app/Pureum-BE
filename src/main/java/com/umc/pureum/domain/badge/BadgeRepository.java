package com.umc.pureum.domain.badge;

import com.umc.pureum.domain.badge.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@EnableJpaRepositories
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    List<Badge> findByUserId(long id);
}
