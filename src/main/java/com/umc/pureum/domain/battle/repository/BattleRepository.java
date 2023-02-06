package com.umc.pureum.domain.battle.repository;

import com.umc.pureum.domain.battle.dto.response.GetBattleInfoRes;
import com.umc.pureum.domain.battle.dto.response.GetBattlesInterface;
import com.umc.pureum.domain.battle.dto.response.GetCompleteBattles;
import com.umc.pureum.domain.battle.dto.response.GetWaitBattlesRes;
import com.umc.pureum.domain.battle.entity.Battle;
import com.umc.pureum.domain.battle.entity.BattleStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface BattleRepository extends JpaRepository<Battle, Long> {
    /* 이미 신청한 키워드인지 확인 */
    @Query("select b from Battle as b \n" +
            "where ((b.challenger.id = :erId and b.challenged.id = :edId) \n" +
            "   or (b.challenger.id = :edId and b.challenged.id = :erId)) \n " +
            "   and b.word.id = :wordId \n " +
            "   and b.status <> 'D'")
    Optional<Battle> findByUserIdAndWordId(@Param("erId") Long erId, @Param("edId") Long edId, @Param("wordId") Long wordId);

    /* 진행 중인 대결 리스트 반환 */
    @Query("select b.id as battleId, b.word.id as keywordId, b.word.word.word as keyword, \n" +
            "   b.challenger.id as challengerId, b.challenger.nickname as challengerNickname, b.challenger.image as challengerProfileImg, \n" +
            "   b.challenged.id as challengedId, b.challenged.nickname as challengedNickname, b.challenged.image as challengedProfileImg \n" +
            "from Battle as b \n" +
            "where b.status = :status")
    List<GetBattlesInterface> findAllBattles(@Param("status") BattleStatus status, PageRequest pageable);

    /* 종료된 대결 리스트 반환 */
    @Query("select b.id as battleId, b.word.id as wordId, b.word.word.word as word, \n" +
            "   case when(r.user.id = b.challenger.id and r.user.status = 'A') then b.challenger.id \n" +
            "        when(r.user.id = b.challenger.id and r.user.status = 'D') then '' \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'A') then b.challenged.id \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'D') then '' \n" +
            "        else 0 \n" +
            "        end as winnerId, \n" +
            "   case when(r.user.id = b.challenger.id and r.user.status = 'A') then b.challenger.nickname \n" +
            "        when(r.user.id = b.challenger.id and r.user.status = 'D') then '' \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'A') then b.challenged.nickname \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'D') then '' \n" +
            "        else '' \n" +
            "        end as winnerNickname, \n" +
            "   case when(r.user.id = b.challenger.id and r.user.status = 'A') then b.challenger.image \n" +
            "        when(r.user.id = b.challenger.id and r.user.status = 'D') then '' \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'A') then b.challenged.image \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'D') then '' \n" +
            "        else b.challenger.image \n" +
            "        end as winnerProfileImg, \n" +
            "   case when(r.user.id = b.challenger.id and r.user.status = 'A') then '' \n" +
            "        when(r.user.id = b.challenger.id and r.user.status = 'D') then '' \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'A') then '' \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'D') then '' \n" +
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
            "   case when(b.challenger.id = :userId) then b.challenged.id \n" +
            "        else b.challenger.id \n" +
            "        end as otherId, \n" +
            "   case when(b.challenger.id = :userId) then b.challenged.nickname \n" +
            "        else b.challenger.nickname \n" +
            "        end as otherNickname, \n" +
            "   case when(b.challenger.id = :userId) then b.challenged.image \n" +
            "        else b.challenger.image \n" +
            "        end as otherProfileImg, \n" +
            "   b.word.id as wordId, b.word.word.word as word, b.duration as duration \n" +
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

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM battle " +
                    "WHERE DATE_FORMAT(updated_at,'%Y-%m-%d')= (DATE_FORMAT(CURDATE(),'%Y-%m-%d')-INTERVAL duration DAY) and status = 'I'")
    List<Battle> findByEndBattle();

    /* 나의 종료된 대결 리스트 반환 */
    @Query("select b.id as battleId, b.word.id as wordId, b.word.word.word as word, \n" +
            "   case when(r.user.id = b.challenger.id and r.user.status = 'A') then b.challenger.id \n" +
            "        when(r.user.id = b.challenger.id and r.user.status = 'D') then '' \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'A') then b.challenged.id \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'D') then '' \n" +
            "        else 0 \n" +
            "        end as winnerId, \n" +
            "   case when(r.user.id = b.challenger.id and r.user.status = 'A') then b.challenger.nickname \n" +
            "        when(r.user.id = b.challenger.id and r.user.status = 'D') then '' \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'A') then b.challenged.nickname \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'D') then '' \n" +
            "        else '' \n" +
            "        end as winnerNickname, \n" +
            "   case when(r.user.id = b.challenger.id and r.user.status = 'A') then b.challenger.image \n" +
            "        when(r.user.id = b.challenger.id and r.user.status = 'D') then '' \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'A') then b.challenged.image \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'D') then '' \n" +
            "        else b.challenger.image \n" +
            "        end as winnerProfileImg, \n" +
            "   case when(r.user.id = b.challenger.id and r.user.status = 'A') then '' \n" +
            "        when(r.user.id = b.challenger.id and r.user.status = 'D') then '' \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'A') then '' \n" +
            "        when(r.user.id = b.challenged.id and r.user.status = 'D') then '' \n" +
            "        else b.challenged.image \n" +
            "        end as otherProfileImg \n" +
            "from Battle as b \n" +
            "left join BattleResult r \n" +
            "   on b.id = r.battle.id \n" +
            "where (b.challenged.id = :userId or b.challenger.id = :userId) \n" +
            "   and b.status = 'C' \n" +
            "   and r.status = 'A'")
    List<GetCompleteBattles> findAllMyCompleteBattles(Long userId, PageRequest request);

    @Query("select b.id as battleId, b.word.id as keywordId, b.word.word.word as keyword, \n" +
            "   b.challenger.id as challengerId, b.challenger.nickname as challengerNickname, b.challenger.image as challengerProfileImg, \n" +
            "   b.challenged.id as challengedId, b.challenged.nickname as challengedNickname, b.challenged.image as challengedProfileImg, \n" +
            "   b.duration as duration, b.status as battleStatus \n" +
            "from Battle as b \n" +
            "where b.id = :battleId")
    List<GetBattleInfoRes> findInfoByBattleId(@Param("battleId") Long battleId);
}
