package com.umc.pureum.domain.blame.repository;

import com.umc.pureum.domain.blame.entity.BattleSentenceBlame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BattleSentenceBlameRepository extends JpaRepository<BattleSentenceBlame, Long> {
    Optional<BattleSentenceBlame> findByBattleSentenceIdAndUserIdAndStatus(long battleSentenceId, long userId, BattleSentenceBlame.Status status);

    List<BattleSentenceBlame> findByBattleSentenceIdAndStatus(long battleSentence, BattleSentenceBlame.Status status);
}
