package com.umc.pureum.domain.sentence.repository;

import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.entity.mapping.SentenceLikeMapping;
import com.umc.pureum.domain.sentence.entity.mapping.SentenceMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface SentenceRepository extends JpaRepository<Sentence, Long> {
    /* 작성 여부 검사 */
    Optional<Sentence> findByKeywordId(Long id);
}
