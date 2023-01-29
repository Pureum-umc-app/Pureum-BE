package com.umc.pureum.domain.battle.repository;

import com.umc.pureum.domain.battle.dto.repsonse.GetWaitBattlesRes;
import com.umc.pureum.domain.battle.entity.Battle;
import com.umc.pureum.domain.battle.entity.BattleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface BattleRepository extends JpaRepository<Battle, Long> {
    /* 대기 중인 대결 리스트 반환 (내가 챌린저인 경우) */
    @Query("select b.id as battleId, \n" +
            "   b.challenger.id as challengerId, b.challenger.nickname as challengerNickname, b.challenger.image as challengerProfileImg, \n" +
            "   b.challenged.id as challengedId, b.challenged.nickname as challengedNickname, b.challenged.image as challengedProfileImg, \n" +
            "   b.word.id as keywordId, b.word.word.word as keyword, \n" +
            "   b.duration as duration \n" +
            "from Battle as b \n" +
            "where b.challenged.id = :userId" +
            "    or b.challenger.id = :userId" +
            "    and b.status = :status")
    List<GetWaitBattlesRes> findAllByWaitBattles(@Param("userId") Long userId, @Param("status")BattleStatus status);
}
