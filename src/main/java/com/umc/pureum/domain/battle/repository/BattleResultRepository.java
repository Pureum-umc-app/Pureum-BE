package com.umc.pureum.domain.battle.repository;

import com.umc.pureum.domain.battle.entity.BattleResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


@Repository
@EnableJpaRepositories
public interface BattleResultRepository extends JpaRepository<BattleResult, Long>{

}
