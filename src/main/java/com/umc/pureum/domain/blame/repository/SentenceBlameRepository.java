package com.umc.pureum.domain.blame.repository;

import com.umc.pureum.domain.blame.entity.BattleSentenceBlame;
import com.umc.pureum.domain.blame.entity.SentenceBlame;
import com.umc.pureum.global.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SentenceBlameRepository extends JpaRepository<SentenceBlame,Long> {
    Optional<SentenceBlame> findBySentenceIdAndUserIdAndStatus(long sentenceId, long userId, Status status);

    List<SentenceBlame> findBySentenceIdAndStatus(long sentenceId, Status status);
}
