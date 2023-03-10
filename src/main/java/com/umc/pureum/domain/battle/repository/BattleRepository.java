package com.umc.pureum.domain.battle.repository;

import com.umc.pureum.domain.battle.dto.repsonse.GetBattlesInterface;
import com.umc.pureum.domain.battle.dto.repsonse.GetCompleteBattlesRes;
import com.umc.pureum.domain.battle.dto.repsonse.GetWaitBattlesRes;
import com.umc.pureum.domain.battle.entity.Battle;
import com.umc.pureum.domain.battle.entity.BattleStatus;
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
public interface BattleRepository extends JpaRepository<Battle, Long> {
    /* 이미 신청한 키워드인지 확인 */
    @Query("select b from Battle as b \n" +
            "where (b.challenged.id = :userId \n" +
            "   or b.challenger.id = :userId) \n " +
            "   and b.word.id = :wordId \n " +
            "   and b.status <> 'D'")
    Optional<Battle> findByUserIdAndWordId(@Param("userId") Long userId, @Param("wordId") Long wordId);

    /* 진행 중인 대결 리스트 반환 */
    @Query("select b.id as battleId, b.word.id as keywordId, b.word.word.word as keyword, \n" +
            "   b.challenger.id as challengerId, b.challenger.nickname as challengerNickname, b.challenger.image as challengerProfileImg, \n" +
            "   b.challenged.id as challengedId, b.challenged.nickname as challengedNickname, b.challenged.image as challengedProfileImg \n" +
            "from Battle as b \n" +
            "where b.status = :status" +
            "   and b.challenger.status = 'A'" +
            "   and b.challenged.status = 'A'")
    List<GetBattlesInterface> findAllByStatus(@Param("status") BattleStatus status);

    /* 종료된 대결 리스트 반환 */
    @Query(value = "select b.id as battleId, b.word.id as wordId, b.word.word.word as word, \n" +
            "case when (r.user.id = b.challenger.id) then b.challenger.id \n" +
            "     when (r.user.id = b.challenged.id) then b.challenged.id \n" +
            "     else 0 \n" +
            "     end as winnerId, \n" +
            "case when (r.user.id = b.challenger.id) then b.challenger.nickname \n" +
            "     when (r.user.id = b.challenged.id) then b.challenged.nickname \n" +
            "     else '' \n" +
            "     end as winnerNickname, \n" +
            "case when (r.user.id = b.challenger.id) then b.challenger.image \n" +
            "     when (r.user.id = b.challenged.id) then b.challenged.image \n" +
            "     else '' \n" +
            "     end as winnerProfileImg \n" +
            "from Battle as b \n" +
            "left join BattleResult as r \n" +
            "   on b.id = r.battle.id \n" +
            "where b.status = 'C'" +
            "   and r.status = 'A'")
    List<GetCompleteBattlesRes> findAllByComplete(@Param("userId") Long userId);

    /* 나의 진행 중인 대결 리스트 반환 */
    @Query("select b.id as battleId, b.word.id as keywordId, b.word.word.word as keyword, \n" +
            "   b.challenger.id as challengerId, b.challenger.nickname as challengerNickname, b.challenger.image as challengerProfileImg, \n" +
            "   b.challenged.id as challengedId, b.challenged.nickname as challengedNickname, b.challenged.image as challengedProfileImg \n" +
            "from Battle as b \n" +
            "where (b.challenged.id = :userId \n" +
            "   or b.challenger.id = :userId) \n" +
            "   and b.status = :status"+
            "   and b.challenger.status = 'A'" +
            "   and b.challenged.status = 'A'")
    List<GetBattlesInterface> findAllByUserIdAndStatus(@Param("userId") Long userId, @Param("status") BattleStatus status);

    /* 대기 중인 대결 리스트 반환 */
    @Query("select b.id as battleId, \n" +
            "   b.challenger.id as challengerId, b.challenger.nickname as challengerNickname, b.challenger.image as challengerProfileImg, \n" +
            "   b.challenged.id as challengedId, b.challenged.nickname as challengedNickname, b.challenged.image as challengedProfileImg, \n" +
            "   b.word.id as keywordId, b.word.word.word as keyword, \n" +
            "   b.duration as duration \n" +
            "from Battle as b \n" +
            "where (b.challenged.id = :userId \n" +
            "    or b.challenger.id = :userId) \n" +
            "    and b.status = :status \n"+
            "   and b.challenger.status = 'A' \n" +
            "   and b.challenged.status = 'A'")
    List<GetWaitBattlesRes> findAllByWaitBattles(@Param("userId") Long userId, @Param("status")BattleStatus status);
}
