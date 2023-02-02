package com.umc.pureum.domain.battle.repository;

import com.umc.pureum.domain.battle.dto.repsonse.GetBattleLikeInterface;
import com.umc.pureum.domain.battle.entity.BattleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface BattleLikeRepository extends JpaRepository<BattleLike, Long> {
    @Query(value = "select count(battle_sentence_id) as likeCnt, \n" +
            "   exists(select * from battle_like where battle_sentence_id = ?2 and user_id = ?1 and status ='A') as isLike \n" +
            "from battle_like \n" +
            "where battle_sentence_id = ?2 \n" +
            "   and status = 'A'", nativeQuery = true)
    Optional<GetBattleLikeInterface> findByUserId(Long userId, Long sentenceId);
}
