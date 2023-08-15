package com.umc.pureum.domain.battle.repository;

import com.umc.pureum.domain.battle.dto.response.*;
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
            "   b.challenged.id as challengedId, b.challenged.nickname as challengedNickname, b.challenged.image as challengedProfileImg, \n" +
            "   (b.duration - datediff(current_timestamp, b.createdAt)) as duration \n" +
            "from Battle as b \n" +
            "where b.status = :status")
    List<GetBattlesInterface> findAllBattles(@Param("status") BattleStatus status, PageRequest request);

    /* 종료된 대결 리스트 반환 */
    @Query(value = "select distinct b.id as battleId, \n" +
            "    bw.id as wordId, w.word as word, \n" +
            "    case when(br.user_id = ur.id and ur.status = 'A') then ur.id \n" +
            "         when(br.user_id = ur.id and ur.status = 'D') then null \n" +
            "         when(br.user_id = ud.id and ud.status = 'A') then ud.id \n" +
            "         when(br.user_id = ud.id and ud.status = 'D') then null \n" +
            "         else 0\n" +
            "    end as winnerId, \n" +
            "    case when(br.user_id = ur.id and ur.status = 'A') then ur.nickname \n" +
            "         when(br.user_id = ur.id and ur.status = 'D') then null \n" +
            "         when(br.user_id = ud.id and ud.status = 'A') then ud.nickname \n" +
            "         when(br.user_id = ud.id and ud.status = 'D') then null \n" +
            "         else null \n" +
            "    end as winnerNickname, \n" +
        "        case when(br.user_id = ur.id and ur.status = 'A') then ur.image\n" +
            "         when(br.user_id = ur.id and ur.status = 'D') then null\n" +
            "         when(br.user_id = ud.id and ud.status = 'A') then ud.image\n" +
            "         when(br.user_id = ud.id and ud.status = 'D') then null\n" +
            "         when(br.user_id IS NULL) then ur.image\n" +
            "         else null\n" +
            "    end as winnerProfileImg,\n" +
            "    IF((br.user_id IS NULL), ud.image, null) as otherProfileImg," +
            "    IF((br.user_id IS NULL), 0, 1) as hasResult \n" +
            "from battle as b \n" +
            "left join battle_result as br \n" +
            "    on b.id = br.battle_id and br.status = 'A' \n" +
            "left join battle_word as bw \n" +
            "    on b.battle_word_id = bw.id \n" +
            "left join word as w\n" +
            "    on bw.word_id = w.id \n" +
            "left join user_account as ur \n" +
            "    on b.challenger_id = ur.id \n" +
            "left join user_account as ud \n" +
            "    on b.challenged_id = ud.id \n" +
            "where b.status = 'C' \n" +
            "order by b.created_at desc ", nativeQuery = true)
    List<GetCompleteBattles> findAllCompleteBattles(PageRequest request);

    /* 나의 대기 중인 대결 리스트 반환 */
    @Query("select b.id as battleId, " +
            "   case when(b.status = 'W' and b.challenger.id = :userId) then '대결 수락 대기 중' \n" +
            "        when(b.status = 'W' and b.challenged.id = :userId) then '대결장이 도착했어요!' \n" +
            "        when(b.status = 'A' and b.challenger.id = :userId) then '대결 문장 작성 대기 중' \n" +
            "        when(b.status = 'A' and b.challenged.id = :userId) then '대결 문장을 작성해주세요!' \n" +
            "        else '이거 받으면 뭔가 잘못된 거임' \n" +
            "        end as status, \n" +
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
            "   b.challenged.id as challengedId, b.challenged.nickname as challengedNickname, b.challenged.image as challengedProfileImg, \n" +
            "   (b.duration - datediff(current_timestamp, b.createdAt)) as duration \n" +
            "from Battle as b \n" +
            "where (b.challenged.id = :userId or b.challenger.id = :userId) \n" +
            "   and b.status = 'I'")
    List<GetBattlesInterface> findAllMyBattles(@Param("userId") Long userId, PageRequest request);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM battle " +
                    "WHERE DATE_FORMAT(updated_at,'%Y-%m-%d') = (DATE_FORMAT(CURDATE(),'%Y-%m-%d')-INTERVAL duration DAY) and status = 'I'")
    List<Battle> findByEndBattle();

    /* 나의 종료된 대결 리스트 반환 */
    @Query(value = "select distinct b.id as battleId, \n" +
            "    bw.id as wordId, w.word as word, \n" +
            "    case when(br.user_id = ur.id and ur.status = 'A') then ur.id \n" +
            "         when(br.user_id = ur.id and ur.status = 'D') then null \n" +
            "         when(br.user_id = ud.id and ud.status = 'A') then ud.id \n" +
            "         when(br.user_id = ud.id and ud.status = 'D') then null \n" +
            "         else 0 \n" +
            "    end as winnerId, \n" +
            "    case when(br.user_id = ur.id and ur.status = 'A') then ur.nickname \n" +
            "         when(br.user_id = ur.id and ur.status = 'D') then null \n" +
            "         when(br.user_id = ud.id and ud.status = 'A') then ud.nickname \n" +
            "         when(br.user_id = ud.id and ud.status = 'D') then null \n" +
            "         else null \n" +
            "    end as winnerNickname, \n" +
            "        case when(br.user_id = ur.id and ur.status = 'A') then ur.image \n" +
            "         when(br.user_id = ur.id and ur.status = 'D') then null \n" +
            "         when(br.user_id = ud.id and ud.status = 'A') then ud.image \n" +
            "         when(br.user_id = ud.id and ud.status = 'D') then null \n" +
            "         when(br.user_id IS NULL) then ur.image \n" +
            "         else null \n" +
            "    end as winnerProfileImg,\n" +
            "    IF((br.user_id IS NULL), ud.image, null) as otherProfileImg,\n" +
            "    case when(br.user_id IS NULL) then 2 \n" +
            "         when(br.user_id = :userId) then 0 \n" +
            "         else 1 \n" +
            "    end as situation \n" +
            "from battle as b \n" +
            "left join battle_result as br \n" +
            "    on b.id = br.battle_id and br.status = 'A' \n" +
            "left join battle_word as bw \n" +
            "    on b.battle_word_id = bw.id \n" +
            "left join word as w\n" +
            "    on bw.word_id = w.id \n" +
            "left join user_account as ur \n" +
            "    on b.challenger_id = ur.id \n" +
            "left join user_account as ud \n" +
            "    on b.challenged_id = ud.id \n" +
            "where b.status = 'C' \n" +
            "   and (b.challenged_id = :userId or b.challenger_id = :userId) \n" +
            "order by b.created_at desc ", nativeQuery = true)
    List<GetMyCompleteBattles> findAllMyCompleteBattles(Long userId, PageRequest request);

    @Query("select b.id as battleId, b.word.id as keywordId, b.word.word.word as keyword, b.updatedAt as updateAt, \n" +
            "   b.challenged.id as challengedId, \n" +
            "case when(b.challenged.status = 'A') then b.challenged.nickname \n" +
            "     when(b.challenged.status = 'D') then '' \n" +
            "     else '' \n" +
            "     end as challengedNickname, \n" +
            "case when(b.challenged.status = 'A') then b.challenged.image \n" +
            "     when(b.challenged.status = 'D') then '' \n" +
            "     else '' \n" +
            "     end as challengedProfileImg, \n" +
            "   b.challenger.id as challengerId, \n" +
            "case when(b.challenger.status = 'A') then b.challenger.nickname \n" +
            "     when(b.challenger.status = 'D') then '' \n" +
            "     else '' \n" +
            "     end as challengerNickname, \n" +
            "case when(b.challenger.status = 'A') then b.challenger.image \n" +
            "     when(b.challenger.status = 'D') then '' \n" +
            "     else '' \n" +
            "     end as challengerProfileImg, \n" +
            "   b.duration as duration, b.status as battleStatus \n" +
            "from Battle as b \n" +
            "where b.id = :battleId")
    Optional<GetBattleInfoRes> findInfoByBattleId(@Param("battleId") Long battleId);

    List<Battle> findByChallengerIdOrChallengedId(long userId, long userId1);
}
