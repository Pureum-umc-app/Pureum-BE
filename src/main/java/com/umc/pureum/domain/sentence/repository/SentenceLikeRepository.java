package com.umc.pureum.domain.sentence.repository;


import com.umc.pureum.domain.sentence.entity.SentenceLike;
import com.umc.pureum.domain.sentence.entity.mapping.SentenceLikeMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface SentenceLikeRepository extends JpaRepository<SentenceLike, Long> {
    /**
     *
     */
    @Query(value =
            "select s.sentence.id                                             as sentence_id,\n" +
            "       s.sentence.sentence                                                as sentence,\n" +
            "       s.sentence.keyword.id as keywordId, "+
                    "s.sentence.user.id as userId,\n"+
                    "s.sentence.user.image as image,\n"+
                    "s.sentence.keyword.word.word as keyword,\n"+
                    "s.sentence.updatedAt as time,\n"+
                    "s.sentence.user.nickname as nickname,\n"+
            "       count(s.sentence.id)                                         as likeNum "+
            "from SentenceLike as s "+
            "where s.sentence.user.status = 'A'and s.sentence.keyword.id = :keywordId and s.user.status='A' " +
            "group by sentence_id")
    List<SentenceLikeMapping> findSentenceLikePageOrderByDate(@Param("keywordId") long word_id, Pageable pageable);

    @Query(nativeQuery = true,value =
            "select exists(\n" +
                    "    select 1\n" +
                    "    from sentence_like\n" +
                    "    where user_id = :userId and sentence_id = :sentenceId and status = 'A'\n" +
                    "           )")
    byte ExistsByUserIdAndSentenceId(
            @Param("userId") long userId,
            @Param("sentenceId") long sentenceId
    );

    List<SentenceLike> findBySentenceId(long sentenceId);

    List<SentenceLike> findByUserId(long userId);

    @EntityGraph(attributePaths = {"user","sentence","sentence.keyword","sentence.keyword.word"})
    List<SentenceLike> findBySentenceIdAndStatusNot(Long sentenceId, String status);
}