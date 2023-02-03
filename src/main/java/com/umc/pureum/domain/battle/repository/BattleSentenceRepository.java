package com.umc.pureum.domain.battle.repository;

import com.umc.pureum.domain.battle.dto.repsonse.GetBattleSentenceInterface;
import com.umc.pureum.domain.battle.entity.BattleSentence;
import com.umc.pureum.global.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import org.springframework.security.core.parameters.P;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface BattleSentenceRepository extends JpaRepository<BattleSentence, Long> {
    /* 대결에 사용한 문장 받아오기 */
    Optional<BattleSentence> findByBattleIdAndUserIdAndStatus(Long battleId, Long userId, Status status);


    @Query(nativeQuery = true,
            value = "select id " +
                    "from battle_sentence " +
                    "where battle_id  = :battleId and user_id = :userId and status ='A'"
    )
    long findIdByBattleIdAndUserIdAndStatus(
            @Param("battleId") Long battleId,
            @Param("userId") Long userId);

    @Query("select b.id as battleSentenceId , b.sentence as battleSentence \n" +
            "from BattleSentence b \n" +
            "where b.battle.id = :battleId" +
            "    and b.user.id = :userId")
    List<GetBattleSentenceInterface> findInfoByBattleIdAndUserId(@Param("battleId") Long battleId , @Param("userId") Long userId);

}
