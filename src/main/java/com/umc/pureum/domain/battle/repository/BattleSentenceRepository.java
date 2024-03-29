package com.umc.pureum.domain.battle.repository;

import com.umc.pureum.domain.battle.dto.response.GetBattleSentenceInterface;
import com.umc.pureum.domain.battle.dto.response.GetBattleWriteSentenceInterface;
import com.umc.pureum.domain.battle.entity.BattleSentence;
import com.umc.pureum.global.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface BattleSentenceRepository extends JpaRepository<BattleSentence, Long> {
    /* 대결에 사용한 문장 받아오기 */
    Optional<BattleSentence> findByBattleIdAndUserIdAndStatus(Long battleId, Long userId, Status status);

    /* 같은 문장이 있는지 검사 */
    Optional<BattleSentence> findBySentenceAndUserIdAndWordIdAndStatus(String sentence, Long userId, Long wordId, Status status);

    @Query(nativeQuery = true,
            value = "select id " +
                    "from battle_sentence " +
                    "where battle_id  = :battleId and user_id = :userId and status ='A'"
    )

    Long findIdByBattleIdAndUserIdAndStatus(@Param("battleId") Long battleId, @Param("userId") Long userId);
    @Query("select b.id as battleSentenceId , b.sentence as battleSentence \n" +
            "from BattleSentence b \n" +
            "where b.battle.id = :battleId" +
            "    and b.user.id = :userId")
    List<GetBattleSentenceInterface> findInfoByBattleIdAndUserId(@Param("battleId") Long battleId , @Param("userId") Long userId);


    @Query("select b.battle as battle , b.battle.word as battleWord ,b.battle.word.word.word as keyword, b.battle.status as battleStatus, \n" +
            "   b.battle.challenged.status as challengedStatus, b.battle.challenger.status as challengerStatus \n" +
            "from BattleSentence b \n" +
            "where b.battle.id = :battleId")
    List<GetBattleWriteSentenceInterface> findInfoByBattleId(@Param("battleId") Long battleId);

    @Query("select b.id as battleSentenceId , b.sentence as battleSentence \n" +
            "from BattleSentence b \n" +
            "where b.battle.word.id = :battleWordId" +
            "    and b.user.id = :userId")
    List<GetBattleSentenceInterface> findInfoByBattleWordIdAndUserId(@Param("battleWordId") Long battleWordId , @Param("userId") Long userId);

    List<BattleSentence> findByUserId(long userId);

    Optional<BattleSentence> findByIdAndStatus(Long id, Status status);
}
