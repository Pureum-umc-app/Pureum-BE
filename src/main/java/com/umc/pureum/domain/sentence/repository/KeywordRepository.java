package com.umc.pureum.domain.sentence.repository;

import com.umc.pureum.domain.sentence.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    /* 중복 검사 */
    Optional<Keyword> findByWordId(Long id);

    /* 오늘의 작성 전 단어 반환 API */
}
