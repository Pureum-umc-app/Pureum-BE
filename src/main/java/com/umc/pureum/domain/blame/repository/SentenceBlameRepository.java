package com.umc.pureum.domain.blame.repository;

import com.umc.pureum.domain.blame.entity.BattleSentenceBlame;
import com.umc.pureum.domain.blame.entity.SentenceBlame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SentenceBlameRepository extends JpaRepository<SentenceBlame,Long> {
    Optional<SentenceBlame> findBySentenceIdAndUserIdAndStatus(long sentenceId, long userId, SentenceBlame.Status status);

    List<SentenceBlame> findBySentenceIdAndStatus(long sentenceId, SentenceBlame.Status status);
}
