package com.umc.pureum.domain.battle.repository;

import com.umc.pureum.domain.battle.entity.BattleSentence;
import com.umc.pureum.domain.battle.entity.BattleWord;
import com.umc.pureum.global.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface BattleSentenceRepository extends JpaRepository<BattleSentence, Long> {
    Optional<BattleSentence> findByWordIdAndStatus(Long wordId, Status status);
}
