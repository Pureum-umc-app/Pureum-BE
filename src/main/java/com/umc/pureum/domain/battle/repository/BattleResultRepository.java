package com.umc.pureum.domain.battle.repository;

import com.umc.pureum.domain.battle.dto.repsonse.GetBattleResultInterface;
import com.umc.pureum.domain.battle.entity.BattleResult;
import com.umc.pureum.domain.battle.entity.BattleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface BattleResultRepository extends JpaRepository<BattleResult, Long> {

    @Query("select b.user.id as userId \n"+
    " from BattleResult b \n" +
    "where b.battle.id = :battleId")
    List<GetBattleResultInterface> findBattleResultByBattleId(@Param("battleId") Long battleId);

}
