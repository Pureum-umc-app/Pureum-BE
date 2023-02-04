package com.umc.pureum.domain.battle.repository;

import com.umc.pureum.domain.battle.dto.repsonse.GetBattlesInterface;
import com.umc.pureum.domain.battle.dto.repsonse.GetCompleteBattles;
import com.umc.pureum.domain.battle.dto.repsonse.GetWaitBattlesRes;
import com.umc.pureum.domain.battle.entity.Battle;
import com.umc.pureum.domain.battle.entity.BattleStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface BattleRepository extends JpaRepository<Battle, Long> {
    /* 이미 신청한 키워드인지 확인 */
    @Query("select b from Battle as b \n" +
            "where (b.challenged.id = :userId or b.challenger.id = :userId) \n " +
            "   and b.word.id = :wordId \n " +
            "   and b.status <> 'D'")
    Optional<Battle> findByUserIdAndWordId(@Param("userId") Long userId, @Param("wordId") Long wordId);

    /* 진행 중인 대결 리스트 반환 */
    @Query("select b.id as battleId, b.word.id as keywordId, b.word.word.word as keyword, \n" +
            "   b.challenger.id as challengerId, b.challenger.nickname as challengerNickname, b.challenger.image as challengerProfileImg, \n" +
            "   b.challenged.id as challengedId, b.challenged.nickname as challengedNickname, b.challenged.image as challengedProfileImg \n" +
            "from Battle as b \n" +
            "where b.status = :status")
    List<GetBattlesInterface> findAllBattles(@Param("status") BattleStatus status, PageRequest pageable);

    /* 종료된 대결 리스트 반환 */
    @Query("select b.id as battleId, b.word.id as wordId, b.word.word.word as word, \n" +
            "   case when(r.user.id = b.challenger.id) then b.challenger.id \n" +
            "        when(r.user.id = b.challenged.id) then b.challenged.id \n" +
            "        else 0 \n" +
            "        end as winnerId, \n" +
            "   case when(r.user.id = b.challenger.id) then b.challenger.nickname \n" +
            "        when(r.user.id = b.challenged.id) then b.challenged.nickname \n" +
            "        else '' \n" +
            "        end as winnerNickname, \n" +
            "   case when(r.user.id = b.challenger.id) then b.challenger.image \n" +
            "        when(r.user.id = b.challenged.id) then b.challenged.image \n" +
            "        else b.challenger.image \n" +
            "        end as winnerProfileImg, \n" +
            "   case when(r.user.id = b.challenger.id) then '' \n" +
            "        when(r.user.id = b.challenged.id) then '' \n" +
            "        else b.challenged.image \n" +
            "        end as otherProfileImg \n" +
            "from Battle as b \n" +
            "left join BattleResult r \n" +
            "   on b.id = r.battle.id \n" +
            "where b.status = 'C' \n" +
            "   and r.status = 'A'")
    List<GetCompleteBattles> findAllCompleteBattles(PageRequest request);

    /* 나의 대기 중인 대결 리스트 반환 */
    @Query("select b.id as battleId, \n" +
            "   b.challenger.id as challengerId, b.challenger.nickname as challengerNickname, b.challenger.image as challengerProfileImg, \n" +
            "   b.challenged.id as challengedId, b.challenged.nickname as challengedNickname, b.challenged.image as challengedProfileImg, \n" +
            "   b.word.id as keywordId, b.word.word.word as keyword, \n" +
            "   b.duration as duration \n" +
            "from Battle as b \n" +
            "where (b.challenged.id = :userId or b.challenger.id = :userId) \n" +
            "    and (b.status = 'W' or b.status = 'A')")
    List<GetWaitBattlesRes> findAllMyWaitBattles(@Param("userId") Long userId, PageRequest request);

    /* 나의 진행 중인 대결 리스트 반환 */
    @Query("select b.id as battleId, b.word.id as keywordId, b.word.word.word as keyword, \n" +
            "   b.challenger.id as challengerId, b.challenger.nickname as challengerNickname, b.challenger.image as challengerProfileImg, \n" +
            "   b.challenged.id as challengedId, b.challenged.nickname as challengedNickname, b.challenged.image as challengedProfileImg \n" +
            "from Battle as b \n" +
            "where (b.challenged.id = :userId or b.challenger.id = :userId) \n" +
            "   and b.status = 'I'")
    List<GetBattlesInterface> findAllMyBattles(@Param("userId") Long userId, PageRequest request);

    /* 나의 종료된 대결 리스트 반환 */
    @Query("select b.id as battleId, b.word.id as wordId, b.word.word.word as word, \n" +
            "   case when(r.user.id = b.challenger.id) then b.challenger.id \n" +
            "        when(r.user.id = b.challenged.id) then b.challenged.id \n" +
            "        else 0 \n" +
            "        end as winnerId, \n" +
            "   case when(r.user.id = b.challenger.id) then b.challenger.nickname \n" +
            "        when(r.user.id = b.challenged.id) then b.challenged.nickname \n" +
            "        else '' \n" +
            "        end as winnerNickname, \n" +
            "   case when(r.user.id = b.challenger.id) then b.challenger.image \n" +
            "        when(r.user.id = b.challenged.id) then b.challenged.image \n" +
            "        else b.challenger.image \n" +
            "        end as winnerProfileImg, \n" +
            "   case when(r.user.id = b.challenger.id) then '' \n" +
            "        when(r.user.id = b.challenged.id) then '' \n" +
            "        else b.challenged.image \n" +
            "        end as otherProfileImg \n" +
            "from Battle as b \n" +
            "left join BattleResult r \n" +
            "   on b.id = r.battle.id \n" +
            "where (b.challenged.id = :userId or b.challenger.id = :userId) \n" +
            "   and b.status = 'C' \n" +
            "   and r.status = 'A'")
    List<GetCompleteBattles> findAllMyCompleteBattles(Long userId, PageRequest request);
}
