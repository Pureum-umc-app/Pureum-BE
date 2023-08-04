package com.umc.pureum.domain.sentence.repository;

import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.entity.mapping.SentenceLikeMapping;
import com.umc.pureum.domain.sentence.entity.mapping.SentenceMapping;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

    List<Sentence> findByUserId(long userId);

    @EntityGraph(attributePaths = {"user","keyword","keyword.word"})
    List<Sentence> findByUserIdAndStatusNot(Long userId, String status);

    @EntityGraph(attributePaths = {"user","keyword","keyword.word"})
    List<Sentence> findByUserIdAndStatus(Long userId, String status);

    Optional<Sentence> findByIdAndStatus(long sentenceId, String a);

    List<Sentence> findByUserIdAndStatusOrderByIdDesc(long userId, String a, PageRequest pageRequest);

    List<Sentence> findByKeywordIdAndStatusOrderByIdDesc(long wordId, String a, PageRequest of);

    List<Sentence> findByKeywordIdAndStatus(long wordId, String a, PageRequest likeCount);

    List<Sentence> findByKeywordIdAndStatusNot(long wordId, String d, PageRequest likeCount);
}
