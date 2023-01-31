package com.umc.pureum.domain.battle;

import com.umc.pureum.domain.battle.dto.repsonse.GetBattleLikeInterface;
import com.umc.pureum.domain.battle.dto.repsonse.GetBattlesInterface;
import com.umc.pureum.domain.battle.dto.repsonse.GetBattlesRes;
import com.umc.pureum.domain.battle.dto.repsonse.GetWaitBattlesRes;
import com.umc.pureum.domain.battle.entity.BattleSentence;
import com.umc.pureum.domain.battle.entity.BattleStatus;
import com.umc.pureum.domain.battle.repository.BattleLikeRepository;
import com.umc.pureum.domain.battle.repository.BattleRepository;
import com.umc.pureum.domain.battle.repository.BattleSentenceRepository;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponseStatus;
import com.umc.pureum.global.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BattleProvider {
    private final BattleDao battleDao;
    private final BattleRepository battleRepository;
    private final BattleSentenceRepository sentenceRepository;
    private final BattleLikeRepository likeRepository;
    private final UserRepository userRepository;

    /* 전체 대결 리스트 반환 API */
    public List<GetBattlesRes> getBattles(Long userId) throws BaseException {
        // 유저 예외 처리
        Optional<UserAccount> user = userRepository.findByIdAndStatus(userId, "A");
        if(user.isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }

        // 배틀 정보를 받아옴
        List<GetBattlesInterface> battles = battleRepository.findAllByStatus(BattleStatus.I);

        // 좋아요 정보를 추가해서 배열을 만들어줌
        if(!battles.isEmpty()) {
            List<GetBattlesRes> getBattlesRes = new ArrayList<>();

            for (GetBattlesInterface battle : battles) {
                // Challenger 문장 받아오기
                Optional<BattleSentence> ers = sentenceRepository.findByBattleIdAndUserIdAndStatus(battle.getBattleId(), battle.getChallengerId(), Status.A);
                if (ers.isEmpty()) {
                    throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
                }
                Long ersId = ers.get().getId();
                Long challengerId = ers.get().getUser().getId();

                Optional<GetBattleLikeInterface> erl = likeRepository.findByUserId(userId, ersId);

                // Challenged 문장 받아오기
                Optional<BattleSentence> eds = sentenceRepository.findByBattleIdAndUserIdAndStatus(battle.getBattleId(), battle.getChallengedId(), Status.A);
                if (eds.isEmpty()) {
                    throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
                }

                Long edsId = eds.get().getId();
                Long challengedId = eds.get().getUser().getId();

                Optional<GetBattleLikeInterface> edl = likeRepository.findByUserId(userId, edsId);

                if(erl.isEmpty() || edl.isEmpty()) throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

                // 배열에 값 넣기
                getBattlesRes.add(new GetBattlesRes(battle.getBattleId(), battle.getKeywordId(), battle.getKeyword(),
                        battle.getChallengerId(), battle.getChallengerNickname(), battle.getChallengerProfileImg(),
                        erl.get().getIsLike(), erl.get().getLikeCnt(),
                        battle.getChallengedId(), battle.getChallengedNickname(), battle.getChallengedProfileImg(),
                        edl.get().getIsLike(), edl.get().getLikeCnt()));

            }

            return getBattlesRes;

        } else {  // 대결이 없으면 빈 배열을 리턴함
            return new ArrayList<>();
        }
    }

    /* 대기 중인 대결 리스트 반환 API */
    public List<GetWaitBattlesRes> getWaitBattles(Long userId) throws BaseException {
        // 유저 예외 처리
        Optional<UserAccount> myInfo = userRepository.findByIdAndStatus(userId, "A");
        if(myInfo.isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }

        return battleRepository.findAllByWaitBattles(userId, BattleStatus.W);
    }
}
