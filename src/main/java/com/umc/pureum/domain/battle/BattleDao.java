package com.umc.pureum.domain.battle;

import com.umc.pureum.domain.battle.dto.repsonse.GetBattleInfoRes;
import com.umc.pureum.domain.battle.dto.repsonse.GetWaitBattlesRes;
import com.umc.pureum.domain.battle.entity.Battle;
import com.umc.pureum.domain.battle.entity.BattleWord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BattleDao {

    private final EntityManager em;

    //대결 단건 조회
    public Battle findOne(Long battleId){
        return em.find(Battle.class, battleId);
    }

    //대결 단어 조회
    public BattleWord findWord(Long battleId){
        return em.find(BattleWord.class, battleId);
    }

    //대결 정보 가져오기
//    public Optional<GetBattleInfoRes> findInfoByBattleId(Long battleId){
//        List<GetBattleInfoRes> battleInfoList = em.createQuery("select b.id as battleId, b.word.id as keywordId, b.word.word.word as keyword, \n" +
//                        "   b.challenger.id as challengerId, b.challenger.nickname as challengerNickname, b.challenger.image as challengerProfileImg, \n" +
//                        "   b.challenged.id as challengedId, b.challenged.nickname as challengedNickname, b.challenged.image as challengedProfileImg, \n" +
//                        "   b.duration as duration, b.status as status \n" +
//                        "from Battle as b \n" +
//                        "where b.id = :battleId" , GetBattleInfoRes.class)
//                .setParameter("battleId", battleId)
//                .getResultList();
//        return battleInfoList.stream().findAny();
//    }


}
