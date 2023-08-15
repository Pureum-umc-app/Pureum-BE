package com.umc.pureum.domain.sentence.repository;

import com.umc.pureum.domain.sentence.dto.response.SentenceListRes;
import com.umc.pureum.domain.sentence.entity.Sentence;
import org.springframework.data.domain.PageRequest;
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

    Optional<Sentence> findByIdAndStatus(Long sentenceId, String a);

    @Query("select s.id as sentenceId, s.sentence as sentence, \n" +
            "   s.keyword.id as keywordId, s.keyword.word.word as keyword, \n" +
            "   s.user.id as userId, s.user.nickname as nickname, s.user.image as profileImg, \n" +
            "   date_format(s.createdAt, '%Y-%m-%d') as date, \n" +
            "   (select count(*) from SentenceLike as sl where s.id=sl.sentence.id and sl.status='A') as likeCnt, \n" +
            "   case when(exists(select sl.id from SentenceLike as sl where s.id=sl.sentence.id and sl.status='A' and sl.user.id=:userId)) then 'T' \n" +
            "       else 'F' \n" +
            "   end as isLiked, \n" +
            "   case when(exists(select sb.id from SentenceBlame as sb where s.id=sb.sentence.id and sb.status='A' and sb.user.id=:userId)) then 'T' \n" +
            "       else 'F' \n" +
            "   end as isBlamed \n" +
            "from Sentence as s \n" +
            "where s.keyword.id=:wordId and s.status='O'")
    List<SentenceListRes> findByWordId(@Param("wordId") Long wordId, @Param("userId") Long userId,  PageRequest request);
}
