package com.umc.pureum.domain.sentence.repository;

import com.umc.pureum.domain.sentence.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    /* 중복 검사 */
    Optional<Keyword> findByWordId(Long id);

    /* 오늘의 단어 반환 API */
    @Query("select k from Keyword as k \n" +
            "where date(k.createdAt) = date(current_timestamp) \n " +
            "   and k.status = 'A'")
    List<Keyword> findByCreatedAt();

    List<Keyword> findByCreatedAtAfter(Timestamp createdAt);

    /* 오늘의 작성 완료 단어 반환 API */
    @Query("select k from Keyword as k \n" +
            "   left join Sentence as s on s.keyword.id = k.id and s.user.id= :userIdx \n" +
            "where date(s.createdAt) = date(current_timestamp) \n" +
            "   and k.status = 'A' \n" +
            "   and s.status = 'O' or s.status = 'P'")
    List<Keyword> findCompleteKeyword(@Param("userIdx") Long userIdx);
}
