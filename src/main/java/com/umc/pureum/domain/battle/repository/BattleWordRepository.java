package com.umc.pureum.domain.battle.repository;

import com.umc.pureum.domain.battle.entity.BattleWord;
import com.umc.pureum.global.entity.Status;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface BattleWordRepository extends JpaRepository<BattleWord, Long> {
    Optional<BattleWord> findByIdAndStatus(Long wordId, Status status);

    @EntityGraph(attributePaths = {"word"})
    List<BattleWord> findTop3ByOrderByCreatedAtDesc();

}
