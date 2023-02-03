package com.umc.pureum.domain.battle;

import com.umc.pureum.domain.battle.dto.BattleFighterRes;
import com.umc.pureum.domain.battle.dto.GetBattleWordRes;
import com.umc.pureum.domain.battle.dto.repsonse.*;
import com.umc.pureum.domain.battle.entity.BattleSentence;
import com.umc.pureum.domain.battle.entity.BattleStatus;
import com.umc.pureum.domain.battle.entity.BattleWord;
import com.umc.pureum.domain.battle.repository.BattleLikeRepository;
import com.umc.pureum.domain.battle.repository.BattleRepository;
import com.umc.pureum.domain.battle.repository.BattleSentenceRepository;
import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Word;
import com.umc.pureum.domain.user.UserDao;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.umc.pureum.global.entity.Status.A;

@RequiredArgsConstructor
@Service
public class BattleProvider {
    private final BattleDao battleDao;
    private final BattleRepository battleRepository;
    private final BattleSentenceRepository sentenceRepository;
    private final BattleLikeRepository likeRepository;
    private final UserRepository userRepository;
    private final UserDao userDao;

    /* 진행 중인 대결 리스트 반환 API */
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
                System.out.println(battle.getBattleId());
                // Challenger 문장 받아오기
                Optional<BattleSentence> ers = sentenceRepository.findByBattleIdAndUserIdAndStatus(battle.getBattleId(), battle.getChallengerId(), A);
                if (ers.isEmpty()) {
                    throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
                }
                Long ersId = ers.get().getId();

                Optional<GetBattleLikeInterface> erl = likeRepository.findByUserId(userId, ersId);

                // Challenged 문장 받아오기
                Optional<BattleSentence> eds = sentenceRepository.findByBattleIdAndUserIdAndStatus(battle.getBattleId(), battle.getChallengedId(), A);
                if (eds.isEmpty()) {
                    throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
                }

                Long edsId = eds.get().getId();

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

    /* 종료된 대결 리스트 반환 */
    public List<GetCompleteBattles> getCompleteBattles(Long userId) throws BaseException {
        // 유저 예외 처리
        Optional<UserAccount> myInfo = userRepository.findByIdAndStatus(userId, "A");
        if(myInfo.isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }

        return battleRepository.findAllByComplete();
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

    /* 나의 진행 중인 대결 리스트 반환 API */
    public List<GetBattlesRes> getMyBattles(Long userId) throws BaseException {
        // 유저 예외 처리
        Optional<UserAccount> user = userRepository.findByIdAndStatus(userId, "A");
        if(user.isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }

        // 배틀 정보를 받아옴
        List<GetBattlesInterface> battles = battleRepository.findAllByUserIdAndStatus(userId, BattleStatus.I);

        // 좋아요 정보를 추가해서 배열을 만들어줌
        if(!battles.isEmpty()) {
            List<GetBattlesRes> getBattlesRes = new ArrayList<>();

            for (GetBattlesInterface battle : battles) {
                // Challenger 문장 받아오기
                Optional<BattleSentence> ers = sentenceRepository.findByBattleIdAndUserIdAndStatus(battle.getBattleId(), battle.getChallengerId(), A);
                if (ers.isEmpty()) {
                    throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
                }
                Long ersId = ers.get().getId();

                Optional<GetBattleLikeInterface> erl = likeRepository.findByUserId(userId, ersId);

                // Challenged 문장 받아오기
                Optional<BattleSentence> eds = sentenceRepository.findByBattleIdAndUserIdAndStatus(battle.getBattleId(), battle.getChallengedId(), A);
                if (eds.isEmpty()) {
                    throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
                }

                Long edsId = eds.get().getId();

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

    /* 나의 종료된 대결 리스트 반환 */
    public List<GetCompleteBattles> getMyCompleteBattles(Long userId) throws BaseException {
        // 유저 예외 처리
        Optional<UserAccount> myInfo = userRepository.findByIdAndStatus(userId, "A");
        if(myInfo.isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }

        return battleRepository.findAllByComplete();
    }

    /* 대결 상대 리스트 반환 API */
    public List<BattleFighterRes> getBattleFighters(Long userId) {
        List<UserAccount> allExcludeMe = userDao.findAllExcludeMe(userId);
        return allExcludeMe.stream().map(u -> BattleFighterRes.builder()
                .userId(u.getId())
                .nickname(u.getNickname())
                .image(u.getImage()).build())
                .collect(Collectors.toList());
    }

    /* 대결 키워드 3개 반환 API */
    public List<GetBattleWordRes> getBattleWordThree(){
        List<BattleWord> battleWordThreeRecently = battleDao.getBattleWordThreeRecently();
        return battleWordThreeRecently.stream().map(b -> GetBattleWordRes.builder()
                .wordId(b.getWord().getId())
                .word(b.getWord().getWord())
                .meaning(b.getWord().getMeaning()).build())
                .collect(Collectors.toList());
    }
}
