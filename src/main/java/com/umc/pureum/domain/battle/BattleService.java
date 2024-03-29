package com.umc.pureum.domain.battle;

import com.umc.pureum.domain.battle.dao.BattleDao;
import com.umc.pureum.domain.battle.dao.BattleLikeDao;
import com.umc.pureum.domain.battle.dao.BattleSentenceDao;
import com.umc.pureum.domain.battle.dto.request.BattleStatusReq;
import com.umc.pureum.domain.battle.dto.request.CreateChallengedSentenceReq;
import com.umc.pureum.domain.battle.dto.request.LikeBattleReq;
import com.umc.pureum.domain.battle.dto.request.PostBattleReq;
import com.umc.pureum.domain.battle.dto.response.*;
import com.umc.pureum.domain.battle.entity.*;
import com.umc.pureum.domain.battle.repository.*;
import com.umc.pureum.domain.blame.entity.BattleSentenceBlame;
import com.umc.pureum.domain.blame.repository.BattleSentenceBlameRepository;
import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Word;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.config.Response.BaseException;
import com.umc.pureum.global.config.Response.BaseResponseStatus;
import com.umc.pureum.global.entity.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.umc.pureum.global.config.Response.BaseResponseStatus.*;
import static com.umc.pureum.global.entity.Status.A;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BattleService {
    private final BattleDao battleDao;
    private final BattleSentenceDao battleSentenceDao;
    private final UserRepository userRepository;
    private final BattleRepository battleRepository;
    private final BattleWordRepository battleWordRepository;
    private final BattleSentenceRepository battleSentenceRepository;
    private final BattleLikeDao battleLikeDao;
    private final BattleLikeRepository likeRepository;
    private final BattleResultRepository battleResultRepository;
    private final BattleLikeRepository battleLikeRepository;
    private final BattleSentenceBlameRepository battleSentenceBlameRepository;

    /* 대결 신청 API */
    @Transactional
    public Long createBattle(PostBattleReq postBattleReq) throws BaseException {
        // 유저 예외 처리
        Optional<UserAccount> challenger = userRepository.findByIdAndStatus(postBattleReq.getChallengerId(), "A");
        if (challenger.isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }
        Optional<UserAccount> challenged = userRepository.findByIdAndStatus(postBattleReq.getChallengedId(), "A");
        if (challenged.isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }
        // 키워드 예외 처리
        Optional<BattleWord> word = battleWordRepository.findByIdAndStatus(postBattleReq.getWordId(), Status.A);
        if (word.isEmpty()) {  // 존재하지 않는 키워드
            throw new BaseException(BaseResponseStatus.POST_BATTLE_NO_EXIST_KEYWORD);
        }
        Optional<Battle> battle = battleRepository.findByUserIdAndWordId(postBattleReq.getChallengerId(), postBattleReq.getChallengedId(), postBattleReq.getWordId());
        if (battle.isPresent()) {  // 이미 대결에 사용한 키워드
            throw new BaseException(BaseResponseStatus.POST_BATTLE_ALREADY_EXIST_KEYWORD);
        }

        // 문장 예외 처리
        Optional<BattleSentence> sentence = battleSentenceRepository.findBySentenceAndUserIdAndWordIdAndStatus(postBattleReq.getSentence(), postBattleReq.getChallengerId(), postBattleReq.getWordId(), A);
        if (sentence.isPresent()) {
            throw new BaseException(POST_BATTLE_ALREADY_EXIST_SENTENCE);
        }

        // 대결 저장
        Battle newBattle = new Battle(challenger.get(), challenged.get(), word.get(), postBattleReq.getDuration(), BattleStatus.W);
        Battle savedBattle = battleRepository.save(newBattle);

        // 문장 저장
        BattleSentence savedSentence = new BattleSentence(savedBattle, challenger.get(),
                postBattleReq.getSentence(), word.get(), Status.A);
        battleSentenceRepository.save(savedSentence);

        /*
        try {
            firebaseCloudMessageService.sendMessageTo(
                    postBattleReq.getChallengedId(),
                    "대결장이 도착했어요",
                    userRepository.findByIdAndStatus(postBattleReq.getChallengerId(),"A").get().getNickname()+"님이 보낸 대결장을 지금 확인해보세요");
        } catch (IOException e) {
            throw new BaseException(BaseResponseStatus.FCM_ERROR);
        }
         */
        return savedBattle.getId();
    }

    /* accept : 대결 수락 API */
    @Transactional
    public BattleStatusRes accept(BattleStatusReq request) {

        // request 로 받은 battleId 로 battle 찾기
        Battle battle = battleRepository.findByIdAndStatus(request.getBattleId(), BattleStatus.W).orElseThrow(() -> new BaseException(NOT_FOUND_WAIT_BATTLE));

        // battle 상태를 수락 완료로 바꾸기
        battle.setStatus(BattleStatus.A);

        // battle ID , Status 값
        return new BattleStatusRes(battle.getId(), battle.getStatus());
    }

    /* reject : 대결 거절 API */
    @Transactional
    public BattleStatusRes reject(BattleStatusReq request) throws BaseException {

        // request 로 받은 battleId 로 battle 찾기
        Battle battle = battleRepository.findByIdAndStatus(request.getBattleId(), BattleStatus.W).orElseThrow(() -> new BaseException(NOT_FOUND_WAIT_BATTLE));

        // battle 상태를 거절로 바꾸기
        battle.setStatus(BattleStatus.D);
        /*
        try {
            firebaseCloudMessageService.sendMessageTo(battle.getChallenger().getId(),"상대가 대결을 거절했어요.","거절했어요");
        } catch (IOException e) {
            throw new BaseException(BaseResponseStatus.FCM_ERROR);
        }
         */
        // battle ID , Status 값
        return new BattleStatusRes(battle.getId(), battle.getStatus());
    }

    /* cancel : 대결 취소 API */
    @Transactional
    public BattleStatusRes cancel(BattleStatusReq request) throws BaseException {

        // request 로 받은 battleId 로 battle 찾기
        Battle battle = battleRepository.findByIdAndStatus(request.getBattleId(), BattleStatus.W).orElseThrow(() -> new BaseException(NOT_FOUND_WAIT_BATTLE));

        // battle 상태를 수락 완료로 바꾸기
        battle.setStatus(BattleStatus.D);
        /*
        try {
            firebaseCloudMessageService.sendMessageTo(battle.getChallenger().getId(),"상대가 대결을 취소했어요.","취소했어요");
        } catch (IOException e) {
            throw new BaseException(BaseResponseStatus.FCM_ERROR);
        }

         */
        // battle ID , Status 값
        return new BattleStatusRes(battle.getId(), battle.getStatus());
    }


    public List<String> BattleMyProfilePhoto(long userId) {
        UserAccount userAccount = userRepository.findByIdAndStatus(userId, "A").get();
        String img = userAccount.getImage();
        String nickname = userAccount.getNickname();
        List<String> list = new ArrayList<>();
        list.add(img);
        list.add(nickname);
        return list;
    }

    /* 대결 받은 사람 대결 문장 작성 API */
    @Transactional
    public CreateChallengedSentenceRes writeChallenged(Long userId, CreateChallengedSentenceReq request) throws BaseException {

        Long battleId = request.getBattleId();
        String writingSentence = request.getSentence();

        GetBattleWriteSentenceInterface getBattleWriteSentenceInterface = battleSentenceRepository.findInfoByBattleId(battleId).stream().findAny().get();

        Battle battle = getBattleWriteSentenceInterface.getBattle();
        BattleWord battleWord = getBattleWriteSentenceInterface.getBattleWord();
        String writingWord = getBattleWriteSentenceInterface.getKeyword();
        BattleStatus battleStatus = getBattleWriteSentenceInterface.getBattleStatus();
        Status challengedStatus = getBattleWriteSentenceInterface.getChallengedStatus();
        Status challengerStatus = getBattleWriteSentenceInterface.getChallengerStatus();

        List<GetBattleSentenceInterface> infoByBattleWordIdAndUserId = battleSentenceRepository.findInfoByBattleWordIdAndUserId(battleWord.getId(), userId);

        // 대결이 종료된 대결인지 여부 확인 && 탈퇴한 회원 여부 확인
        if (battleStatus == BattleStatus.C || battleStatus == BattleStatus.D || challengedStatus != Status.A || challengerStatus != Status.A) {
            throw new BaseException(GET_BATTLE_FINISH_STATUS);
        } else if (battleStatus == BattleStatus.W) {
            throw new BaseException(Get_BATTLE_NO_ACCEPT_STATUS);
        }

        // 작성한 문장 존재 여부 확인
        if (Objects.equals(writingSentence, "")) {
            throw new BaseException(POST_SENTENCE_EMPTY);
        }

        // 작성할 문장에 단어 포함 여부 확인
        if (!isKeywordExist(writingSentence, writingWord)) {
            throw new BaseException(POST_SENTENCE_NO_EXISTS_KEYWORD);
        }

        // 작성한 문장이 동일한 문장인지 여부 확인
        if (isSentenceExist(writingSentence, infoByBattleWordIdAndUserId)) {
            throw new BaseException(POST_SENTENCE_EXISTS);
        }

        // request 로 받은 userId 로 userAccount 찾기
        UserAccount userAccount = userRepository.findById(userId).get();

        BattleSentence battleSentence = new BattleSentence(battle, userAccount, writingSentence, battleWord, Status.A);
        battleSentenceDao.save(battleSentence);

        // battle status 바꾸기
        battle.setStatus(BattleStatus.A);

        /*
        try {
            firebaseCloudMessageService.sendMessageTo(battle.getChallenger().getId(),"상대가 대결을 수락했어요.","이겨보아요");
        } catch (IOException e) {
            throw new BaseException(BaseResponseStatus.FCM_ERROR);
        }
         */

        return new CreateChallengedSentenceRes(battleSentence.getId(), battle.getId(), battle.getStatus());
    }


    /* Sentence 내에 Keyword 존재여부 검사 */
    // isKeywordExist : 문장에 키워드가 포함되어있는지 확인하는 함수
    private boolean isKeywordExist(String writingSentence, String writingWord) {
        return writingSentence.contains(writingWord);
    }

    // isSentenceExist : 대결 단어에 대해 같은 문장의 여부 확인
    private boolean isSentenceExist(String writingSentence, List<GetBattleSentenceInterface> infoByBattleWordIdAndUserId) {
        for (int i = 0; i < infoByBattleWordIdAndUserId.size(); i++) {
            if (infoByBattleWordIdAndUserId.get(i).getBattleSentence().equals(writingSentence)) {
                return true;
            }
        }
        return false;
    }

    /* 대결 문장 좋아요, 좋아요 취소 API */
    @Transactional
    public LikeBattleRes like(long userId, LikeBattleReq request) {

        // request 로 받은 sentenceId 로 문장 찾기
        BattleSentence battleSentence = battleSentenceDao.findOne(request.getSentenceId());

        // request 로 받은 userId 로 userAccount 찾기
        UserAccount userAccount = userRepository.findById(userId).get();

        //request 로 받은 sentenceId 로 문장 좋아요 찾기
        if (battleLikeDao.findBySentenceId(request.getSentenceId(), userId).isPresent()) {

            BattleLike battleLike = battleLikeDao.findBySentenceId(request.getSentenceId(), userId).get();

            // 존재하는 sentence 일 경우 sentence status 확인하고 status 바꾼다 .
            if (Status.A.equals(battleLike.getStatus())) {
                battleLike.setStatus(Status.D);
            } else if (Status.D.equals(battleLike.getStatus())) {
                battleLike.setStatus(Status.A);
            }

            return new LikeBattleRes(battleLike.getId(), battleLike.getStatus());

        }

        // 존재하지 않는 sentence 일 경우 sentenceLike 생성해서 저장
        else {
            BattleLike battleLike = new BattleLike(userAccount, battleSentence, Status.A);
            battleLikeDao.save(battleLike);

            return new LikeBattleRes(battleLike.getId(), battleLike.getStatus());
        }

    }

    /* 대결 정보 반환 API (대기 중, 진행 중) */
    @Transactional
    public ReturnRunBattleRes returnRunBattle(long battleIdx, Long userId) throws BaseException {
        if (userRepository.findByIdAndStatus(userId, "A").isEmpty()) {
            throw new BaseException(INVALID_USER);
        }

        Optional<GetBattleInfoRes> battle = battleRepository.findInfoByBattleId(battleIdx);
        if (battle.isEmpty()) {
            throw new BaseException(Get_BATTLE_RUN_STATUS);
        }
        GetBattleInfoRes battleInfo = battle.get();

        // 대결 상태 확인
        if (battleInfo.getBattleStatus().equals(BattleStatus.W)) {
            Timestamp updateAt = battleInfo.getUpdateAt();
            LocalDateTime currentLocalDateTime = LocalDateTime.now();
            long differ = (Timestamp.valueOf(currentLocalDateTime).getTime() - updateAt.getTime()) / (24 * 60 * 60 * 1000);

            Long challengerId = battleInfo.getChallengerId();

            GetBattleSentenceInterface challengerSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengerId).stream().findAny().get();

            Long challengerSentenceId = challengerSentenceInfo.getBattleSentenceId();
            String remainDuration = "D-";

            if (battleInfo.getDuration() - Long.valueOf(differ).intValue() > 0) {
                remainDuration = remainDuration + (battleInfo.getDuration() - Long.valueOf(differ).intValue());
            } else if (battleInfo.getDuration() - Long.valueOf(differ).intValue() == 0) {
                remainDuration = "D-DAY";
            } else {
                throw new BaseException(GET_BATTLE_FINISH_STATUS);
            }

            GetBattleLikeInterface challengerLikeInterface = likeRepository.findByUserId(userId, challengerSentenceId).stream().findAny().get();

            return new ReturnRunBattleRes(battleInfo.getBattleId(), battleInfo.getKeywordId(), battleInfo.getKeyword(), remainDuration,
                    battleInfo.getChallengedId(), battleInfo.getChallengedNickname(), battleInfo.getChallengedProfileImg(),
                    battleInfo.getChallengerId(), battleInfo.getChallengerNickname(), battleInfo.getChallengerProfileImg(),
                    battleInfo.getDuration(), battleInfo.getBattleStatus(),
                    null, null, null,
                    challengerSentenceInfo.getBattleSentenceId(), challengerSentenceInfo.getBattleSentence(), null,
                    null, challengerLikeInterface.getLikeCnt(),
                    0, challengerLikeInterface.getIsLike()
            );

        } else if (battleInfo.getBattleStatus().equals(BattleStatus.A)) {
            Timestamp updateAt = battleInfo.getUpdateAt();
            LocalDateTime currentLocalDateTime = LocalDateTime.now();
            long differ = (Timestamp.valueOf(currentLocalDateTime).getTime() - updateAt.getTime()) / (24 * 60 * 60 * 1000);

            Long challengedId = battleInfo.getChallengedId();
            Long challengerId = battleInfo.getChallengerId();

            GetBattleSentenceInterface challengedSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengedId).stream().findAny().get();
            GetBattleSentenceInterface challengerSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengerId).stream().findAny().get();

            Long challengedSentenceId = challengedSentenceInfo.getBattleSentenceId();
            Long challengerSentenceId = challengerSentenceInfo.getBattleSentenceId();

            String remainDuration = "D-";

            if (battleInfo.getDuration() - Long.valueOf(differ).intValue() > 0) {
                remainDuration = remainDuration + (battleInfo.getDuration() - Long.valueOf(differ).intValue());
            } else if (battleInfo.getDuration() - Long.valueOf(differ).intValue() == 0) {
                remainDuration = "D-DAY";
            } else {
                throw new BaseException(GET_BATTLE_FINISH_STATUS);
            }
            boolean challengedSentenceBlamed = battleSentenceBlameRepository.findByBattleSentenceIdAndUserIdAndStatus(challengedSentenceId, challengedId, BattleSentenceBlame.Status.A).isPresent();
            boolean challengerSentenceBlamed = battleSentenceBlameRepository.findByBattleSentenceIdAndUserIdAndStatus(challengerSentenceId, challengerId, BattleSentenceBlame.Status.A).isPresent();
            GetBattleLikeInterface challengedLikeInterface = likeRepository.findByUserId(userId, challengedSentenceId).stream().findAny().get();
            GetBattleLikeInterface challengerLikeInterface = likeRepository.findByUserId(userId, challengerSentenceId).stream().findAny().get();
            return new ReturnRunBattleRes(battleInfo.getBattleId(), battleInfo.getKeywordId(), battleInfo.getKeyword(), remainDuration,
                    battleInfo.getChallengedId(), battleInfo.getChallengedNickname(), battleInfo.getChallengedProfileImg(),
                    battleInfo.getChallengerId(), battleInfo.getChallengerNickname(), battleInfo.getChallengerProfileImg(),
                    battleInfo.getDuration(), battleInfo.getBattleStatus(),
                    challengedSentenceInfo.getBattleSentenceId(), challengedSentenceInfo.getBattleSentence(), challengedSentenceBlamed,
                    challengerSentenceInfo.getBattleSentenceId(), challengerSentenceInfo.getBattleSentence(), challengerSentenceBlamed,
                    challengedLikeInterface.getLikeCnt(), challengerLikeInterface.getLikeCnt(),
                    challengedLikeInterface.getIsLike(), challengerLikeInterface.getIsLike());
        } else {
            throw new BaseException(GET_BATTLE_FINISH_STATUS);
        }
    }

    /* 종료된 대결 정보 반환 API */
    @Transactional
    public ReturnFinishBattleRes returnFinishBattle(long battleIdx, Long userId) throws BaseException {
        if (userRepository.findByIdAndStatus(userId, "A").isEmpty()) {
            throw new BaseException(INVALID_USER);
        }
        GetBattleInfoRes battleInfo = battleRepository.findInfoByBattleId(battleIdx).stream().findAny().get();

        GetBattleResultInterface getBattleResultInterface;
        Long winnerUserId;

        if (battleInfo.getBattleStatus().equals(BattleStatus.C) || battleInfo.getBattleStatus().equals(BattleStatus.D)) {

            if (battleResultRepository.findBattleResultByBattleId(battleIdx).get(0) != null) {

                getBattleResultInterface = battleResultRepository.findBattleResultByBattleId(battleIdx).stream().findAny().get();

                winnerUserId = getBattleResultInterface.getUserId();

                Long challengedId = battleInfo.getChallengedId();
                Long challengerId = battleInfo.getChallengerId();

                GetBattleSentenceInterface challengedSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengedId).stream().findAny().get();
                GetBattleSentenceInterface challengerSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengerId).stream().findAny().get();

                Long challengedSentenceId = challengedSentenceInfo.getBattleSentenceId();
                Long challengerSentenceId = challengerSentenceInfo.getBattleSentenceId();

                if (winnerUserId == challengedId) {

                    GetBattleLikeInterface challengedLikeInterface = likeRepository.findByUserId(challengedId, challengedSentenceId).stream().findAny().get();
                    GetBattleLikeInterface challengerLikeInterface = likeRepository.findByUserId(challengerId, challengerSentenceId).stream().findAny().get();
                    GetBattleLikeInterface oppLikeInterface = likeRepository.findByUserId(challengedId, challengerSentenceId).stream().findAny().get();

                    return new ReturnFinishBattleRes(battleIdx, situation(winnerUserId, userId), winnerUserId,
                            battleInfo.getChallengedId(), battleInfo.getChallengedNickname(), battleInfo.getChallengedProfileImg(),
                            battleInfo.getChallengerId(), battleInfo.getChallengerNickname(), battleInfo.getChallengerProfileImg(),
                            battleInfo.getDuration(),
                            challengedSentenceInfo.getBattleSentenceId(), challengedSentenceInfo.getBattleSentence(),
                            challengerSentenceInfo.getBattleSentenceId(), challengerSentenceInfo.getBattleSentence(),
                            challengedLikeInterface.getLikeCnt(), challengerLikeInterface.getLikeCnt(),
                            challengerLikeInterface.getIsLike(), oppLikeInterface.getIsLike()
                    );
                } else if (winnerUserId == challengerId) {

                    GetBattleLikeInterface challengedLikeInterface = likeRepository.findByUserId(challengedId, challengedSentenceId).stream().findAny().get();
                    GetBattleLikeInterface challengerLikeInterface = likeRepository.findByUserId(challengerId, challengerSentenceId).stream().findAny().get();
                    GetBattleLikeInterface oppLikeInterface = likeRepository.findByUserId(challengerId, challengedSentenceId).stream().findAny().get();

                    return new ReturnFinishBattleRes(battleIdx, situation(winnerUserId, userId), winnerUserId,
                            battleInfo.getChallengerId(), battleInfo.getChallengerNickname(), battleInfo.getChallengerProfileImg(),
                            battleInfo.getChallengedId(), battleInfo.getChallengedNickname(), battleInfo.getChallengedProfileImg(),
                            battleInfo.getDuration(),
                            challengerSentenceInfo.getBattleSentenceId(), challengerSentenceInfo.getBattleSentence(),
                            challengedSentenceInfo.getBattleSentenceId(), challengedSentenceInfo.getBattleSentence(),
                            challengerLikeInterface.getLikeCnt(), challengedLikeInterface.getLikeCnt(),
                            challengerLikeInterface.getIsLike(), oppLikeInterface.getIsLike()
                    );
                }
            } else {

                Long challengedId = battleInfo.getChallengedId();
                Long challengerId = battleInfo.getChallengerId();

                GetBattleSentenceInterface challengedSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengedId).stream().findAny().get();
                GetBattleSentenceInterface challengerSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengerId).stream().findAny().get();

                Long challengedSentenceId = challengedSentenceInfo.getBattleSentenceId();
                Long challengerSentenceId = challengerSentenceInfo.getBattleSentenceId();

                GetBattleLikeInterface challengedLikeInterface = likeRepository.findByUserId(challengedId, challengedSentenceId).stream().findAny().get();
                GetBattleLikeInterface challengerLikeInterface = likeRepository.findByUserId(challengerId, challengerSentenceId).stream().findAny().get();
                GetBattleLikeInterface oppLikeInterface = likeRepository.findByUserId(challengedId, challengerSentenceId).stream().findAny().get();

                return new ReturnFinishBattleRes(battleIdx, 2, 0L,
                        battleInfo.getChallengedId(), battleInfo.getChallengedNickname(), battleInfo.getChallengedProfileImg(),
                        battleInfo.getChallengerId(), battleInfo.getChallengerNickname(), battleInfo.getChallengerProfileImg(),
                        battleInfo.getDuration(),
                        challengedSentenceInfo.getBattleSentenceId(), challengedSentenceInfo.getBattleSentence(),
                        challengerSentenceInfo.getBattleSentenceId(), challengerSentenceInfo.getBattleSentence(),
                        challengedLikeInterface.getLikeCnt(), challengerLikeInterface.getLikeCnt(),
                        challengerLikeInterface.getIsLike(), oppLikeInterface.getIsLike()
                );

            }

        } else {
            throw new BaseException(Get_BATTLE_RUN_STATUS);
        }
        return null;
    }

    /* 내 대결 정보 반환 API (대기 중, 진행 중) */
//    @Transactional
//    public ReturnRunMyBattleRes returnRunMyBattle(long battleIdx, Long userId) throws BaseException {
//
//        GetBattleInfoRes battleInfo = battleRepository.findInfoByBattleId(battleIdx).stream().findAny().get();
//
//        // 대결 상태 확인
//        if (battleInfo.getBattleStatus().equals(BattleStatus.W) || battleInfo.getBattleStatus().equals(BattleStatus.A) || battleInfo.getBattleStatus().equals(BattleStatus.I)) {
//            Timestamp updateAt = battleInfo.getUpdateAt();
//            LocalDateTime currentLocalDateTime = LocalDateTime.now();
//            long differ = (Timestamp.valueOf(currentLocalDateTime).getTime() - updateAt.getTime()) / (24 * 60 * 60 * 1000);
//
//            Long challengedId = battleInfo.getChallengedId();
//            Long challengerId = battleInfo.getChallengerId();
//
//            GetBattleSentenceInterface challengedSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengedId).stream().findAny().get();
//            GetBattleSentenceInterface challengerSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengerId).stream().findAny().get();
//
//            Long challengedSentenceId = challengedSentenceInfo.getBattleSentenceId();
//            Long challengerSentenceId = challengerSentenceInfo.getBattleSentenceId();
//
//
//            String remainDuration = "D-";
//
//            if (battleInfo.getDuration() - Long.valueOf(differ).intValue() > 0) {
//                remainDuration = remainDuration + Integer.toString(battleInfo.getDuration() - Long.valueOf(differ).intValue());
//            } else if (battleInfo.getDuration() - Long.valueOf(differ).intValue() == 0) {
//                remainDuration = "D-DAY";
//            } else {
//                throw new BaseException(GET_BATTLE_FINISH_STATUS);
//            }
//            boolean blamed;
//            if (Objects.equals(userId, challengedId)) {
//
//                GetBattleLikeInterface challengedLikeInterface = likeRepository.findByUserId(challengedId, challengedSentenceId).stream().findAny().get();
//                GetBattleLikeInterface challengerLikeInterface = likeRepository.findByUserId(challengerId, challengerSentenceId).stream().findAny().get();
//                if (Objects.equals(challengedSentenceId, userId)) {
//                    blamed = battleSentenceBlameRepository.findByBattleSentenceIdAndUserIdAndStatus(challengedSentenceInfo.getBattleSentenceId(), userId, BattleSentenceBlame.Status.A).isPresent();
//
//                } else if (Objects.equals(challengerSentenceId, userId)) {
//                    blamed = battleSentenceBlameRepository.findByBattleSentenceIdAndUserIdAndStatus(challengerSentenceInfo.getBattleSentenceId(), userId, BattleSentenceBlame.Status.A).isPresent();
//                } else {
//                    throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
//                }
//                return new ReturnRunMyBattleRes(battleInfo.getBattleId(), battleInfo.getKeywordId(), battleInfo.getKeyword(), remainDuration,
//                        battleInfo.getChallengedId(), battleInfo.getChallengedNickname(), battleInfo.getChallengedProfileImg(),
//                        battleInfo.getChallengerId(), battleInfo.getChallengerNickname(), battleInfo.getChallengerProfileImg(),
//                        battleInfo.getDuration(), battleInfo.getBattleStatus(),
//                        challengedSentenceInfo.getBattleSentenceId(), challengedSentenceInfo.getBattleSentence(),
//                        challengerSentenceInfo.getBattleSentenceId(), challengerSentenceInfo.getBattleSentence(),
//                        challengedLikeInterface.getLikeCnt(), challengerLikeInterface.getLikeCnt(),
//                        challengedLikeInterface.getIsLike(), challengerLikeInterface.getIsLike(),blamed
//                );
//            } else if (Objects.equals(userId, challengerId)) {
//                if (Objects.equals(challengedSentenceId, userId)) {
//                    blamed = battleSentenceBlameRepository.findByBattleSentenceIdAndUserIdAndStatus(challengedSentenceInfo.getBattleSentenceId(), userId, BattleSentenceBlame.Status.A).isPresent();
//
//                } else if (Objects.equals(challengerSentenceId, userId)) {
//                    blamed = battleSentenceBlameRepository.findByBattleSentenceIdAndUserIdAndStatus(challengerSentenceInfo.getBattleSentenceId(), userId, BattleSentenceBlame.Status.A).isPresent();
//                } else {
//                    throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
//                }
//                GetBattleLikeInterface challengedLikeInterface = likeRepository.findByUserId(challengedId, challengedSentenceId).stream().findAny().get();
//                GetBattleLikeInterface challengerLikeInterface = likeRepository.findByUserId(challengerId, challengerSentenceId).stream().findAny().get();
//
//                return new ReturnRunMyBattleRes(battleInfo.getBattleId(), battleInfo.getKeywordId(), battleInfo.getKeyword(), remainDuration,
//                        battleInfo.getChallengerId(), battleInfo.getChallengerNickname(), battleInfo.getChallengerProfileImg(),
//                        battleInfo.getChallengedId(), battleInfo.getChallengedNickname(), battleInfo.getChallengedProfileImg(),
//                        battleInfo.getDuration(), battleInfo.getBattleStatus(),
//                        challengerSentenceInfo.getBattleSentenceId(), challengerSentenceInfo.getBattleSentence(),
//                        challengedSentenceInfo.getBattleSentenceId(), challengedSentenceInfo.getBattleSentence(),
//                        challengerLikeInterface.getLikeCnt(), challengedLikeInterface.getLikeCnt(),
//                        challengerLikeInterface.getIsLike(), challengedLikeInterface.getIsLike(),blamed
//                );
//            }
//        } else {
//            throw new BaseException(GET_BATTLE_FINISH_STATUS);
//        }
//        return null;
//    }

    /* 내 종료된 대결 정보 반환 API */
    @Transactional
    public ReturnFinishBattleRes returnFinishMyBattle(long battleIdx, Long userId) throws BaseException {
        GetBattleInfoRes battleInfo = battleRepository.findInfoByBattleId(battleIdx).stream().findAny().get();

        GetBattleResultInterface getBattleResultInterface;
        Long winnerUserId;

        if (battleInfo.getBattleStatus().equals(BattleStatus.C) || battleInfo.getBattleStatus().equals(BattleStatus.D)) {

            if (battleResultRepository.findBattleResultByBattleId(battleIdx).get(0) != null) {

                getBattleResultInterface = battleResultRepository.findBattleResultByBattleId(battleIdx).stream().findAny().get();

                winnerUserId = getBattleResultInterface.getUserId();

                Long challengedId = battleInfo.getChallengedId();
                Long challengerId = battleInfo.getChallengerId();

                GetBattleSentenceInterface challengedSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengedId).stream().findAny().get();
                GetBattleSentenceInterface challengerSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengerId).stream().findAny().get();

                Long challengedSentenceId = challengedSentenceInfo.getBattleSentenceId();
                Long challengerSentenceId = challengerSentenceInfo.getBattleSentenceId();

                if (winnerUserId == challengedId) {

                    GetBattleLikeInterface challengedLikeInterface = likeRepository.findByUserId(challengedId, challengedSentenceId).stream().findAny().get();
                    GetBattleLikeInterface challengerLikeInterface = likeRepository.findByUserId(challengerId, challengerSentenceId).stream().findAny().get();
                    GetBattleLikeInterface oppLikeInterface = likeRepository.findByUserId(challengedId, challengerSentenceId).stream().findAny().get();

                    return new ReturnFinishBattleRes(battleIdx, situation(winnerUserId, userId), winnerUserId,
                            battleInfo.getChallengedId(), battleInfo.getChallengedNickname(), battleInfo.getChallengedProfileImg(),
                            battleInfo.getChallengerId(), battleInfo.getChallengerNickname(), battleInfo.getChallengerProfileImg(),
                            battleInfo.getDuration(),
                            challengedSentenceInfo.getBattleSentenceId(), challengedSentenceInfo.getBattleSentence(),
                            challengerSentenceInfo.getBattleSentenceId(), challengerSentenceInfo.getBattleSentence(),
                            challengedLikeInterface.getLikeCnt(), challengerLikeInterface.getLikeCnt(),
                            challengerLikeInterface.getIsLike(), oppLikeInterface.getIsLike()
                    );
                } else if (winnerUserId == challengerId) {

                    GetBattleLikeInterface challengedLikeInterface = likeRepository.findByUserId(challengedId, challengedSentenceId).stream().findAny().get();
                    GetBattleLikeInterface challengerLikeInterface = likeRepository.findByUserId(challengerId, challengerSentenceId).stream().findAny().get();
                    GetBattleLikeInterface oppLikeInterface = likeRepository.findByUserId(challengerId, challengedSentenceId).stream().findAny().get();

                    return new ReturnFinishBattleRes(battleIdx, situation(winnerUserId, userId), winnerUserId,
                            battleInfo.getChallengerId(), battleInfo.getChallengerNickname(), battleInfo.getChallengerProfileImg(),
                            battleInfo.getChallengedId(), battleInfo.getChallengedNickname(), battleInfo.getChallengedProfileImg(),
                            battleInfo.getDuration(),
                            challengerSentenceInfo.getBattleSentenceId(), challengerSentenceInfo.getBattleSentence(),
                            challengedSentenceInfo.getBattleSentenceId(), challengedSentenceInfo.getBattleSentence(),
                            challengerLikeInterface.getLikeCnt(), challengedLikeInterface.getLikeCnt(),
                            challengerLikeInterface.getIsLike(), oppLikeInterface.getIsLike()
                    );
                }
            } else {

                Long challengedId = battleInfo.getChallengedId();
                Long challengerId = battleInfo.getChallengerId();

                GetBattleSentenceInterface challengedSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengedId).stream().findAny().get();
                GetBattleSentenceInterface challengerSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengerId).stream().findAny().get();

                Long challengedSentenceId = challengedSentenceInfo.getBattleSentenceId();
                Long challengerSentenceId = challengerSentenceInfo.getBattleSentenceId();

                if (userId == challengedId) {

                    GetBattleLikeInterface challengedLikeInterface = likeRepository.findByUserId(challengedId, challengedSentenceId).stream().findAny().get();
                    GetBattleLikeInterface challengerLikeInterface = likeRepository.findByUserId(challengerId, challengerSentenceId).stream().findAny().get();
                    GetBattleLikeInterface oppLikeInterface = likeRepository.findByUserId(challengedId, challengerSentenceId).stream().findAny().get();

                    return new ReturnFinishBattleRes(battleIdx, 2, 0L,
                            battleInfo.getChallengedId(), battleInfo.getChallengedNickname(), battleInfo.getChallengedProfileImg(),
                            battleInfo.getChallengerId(), battleInfo.getChallengerNickname(), battleInfo.getChallengerProfileImg(),
                            battleInfo.getDuration(),
                            challengedSentenceInfo.getBattleSentenceId(), challengedSentenceInfo.getBattleSentence(),
                            challengerSentenceInfo.getBattleSentenceId(), challengerSentenceInfo.getBattleSentence(),
                            challengedLikeInterface.getLikeCnt(), challengerLikeInterface.getLikeCnt(),
                            challengerLikeInterface.getIsLike(), oppLikeInterface.getIsLike()
                    );
                } else if (userId == challengerId) {

                    GetBattleLikeInterface challengedLikeInterface = likeRepository.findByUserId(challengedId, challengedSentenceId).stream().findAny().get();
                    GetBattleLikeInterface challengerLikeInterface = likeRepository.findByUserId(challengerId, challengerSentenceId).stream().findAny().get();
                    GetBattleLikeInterface oppLikeInterface = likeRepository.findByUserId(challengerId, challengedSentenceId).stream().findAny().get();

                    return new ReturnFinishBattleRes(battleIdx, 2, 0L,
                            battleInfo.getChallengerId(), battleInfo.getChallengerNickname(), battleInfo.getChallengerProfileImg(),
                            battleInfo.getChallengedId(), battleInfo.getChallengedNickname(), battleInfo.getChallengedProfileImg(),
                            battleInfo.getDuration(),
                            challengerSentenceInfo.getBattleSentenceId(), challengerSentenceInfo.getBattleSentence(),
                            challengedSentenceInfo.getBattleSentenceId(), challengedSentenceInfo.getBattleSentence(),
                            challengerLikeInterface.getLikeCnt(), challengedLikeInterface.getLikeCnt(),
                            challengerLikeInterface.getIsLike(), oppLikeInterface.getIsLike()
                    );
                }

            }

        } else {
            throw new BaseException(Get_BATTLE_RUN_STATUS);
        }
        return null;
    }

    @Transactional
    // battleWord 에 단어 3개 넣기
    public void saveBattleWordRandomThree() {
        // 중복 검사
        // keyword, battleWord 테이블에서 wordId 만 뽑아서 Long 배열 만들기
        List<Keyword> keywordTable = battleDao.getKeywordTable();
        Long[] keywordWordId = keywordTable.stream().mapToLong(k -> k.getWord().getId()).boxed().toArray(Long[]::new);

        List<BattleWord> battleWordTable = battleDao.getBattleWordTable();
        Long[] battleWordWordId = battleWordTable.stream().mapToLong(b -> b.getWord().getId()).boxed().toArray(Long[]::new);

        // 각각 만든 Long 배열 합치기
        Long[] wordId = Stream.of(keywordWordId, battleWordWordId).flatMap(Stream::of).toArray(Long[]::new);

        // 배열 -> List
        List<Long> wordIdLongToList = Arrays.asList(wordId);

        List<Word> randomThreeBattleWord = battleDao.getRandomThreeBattleWord(wordIdLongToList);

        List<BattleWord> battleWords = randomThreeBattleWord.stream().map(r -> BattleWord.builder()
                        .word(r)
                        .status(A).build())
                .collect(Collectors.toList());

        for (BattleWord battleWord : battleWords) {
            battleDao.saveBattleWord(battleWord);
        }

    }

    /* 대결 종료 */
    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void setResult() {
        List<Battle> battleIdList = battleRepository.findByEndBattle();
        BattleResult battleResult;

        for (Battle battle : battleIdList) {
            battle.setStatus(BattleStatus.C);
            battleRepository.save(battle);

            Long challengedBattleSentenceId = battleSentenceRepository.findIdByBattleIdAndUserIdAndStatus(battle.getId(), battle.getChallenged().getId());
            int challengedLikeNum = battleLikeRepository.CountLikeNumBySentenceId(challengedBattleSentenceId);

            Long challengerBattleSentenceId = battleSentenceRepository.findIdByBattleIdAndUserIdAndStatus(battle.getId(), battle.getChallenger().getId());
            int challengerLikeNum = battleLikeRepository.CountLikeNumBySentenceId(challengerBattleSentenceId);

            if (challengedLikeNum > challengerLikeNum) {
                battleResult = BattleResult.builder()
                        .battle(battle)
                        .user(battle.getChallenged())
                        .status(Status.A)
                        .build();
                battleResultRepository.save(battleResult);
            } else if (challengedLikeNum < challengerLikeNum) {
                battleResult = BattleResult.builder()
                        .battle(battle)
                        .user(battle.getChallenger())
                        .status(Status.A)
                        .build();
                battleResultRepository.save(battleResult);
            } else {
                battleResult = BattleResult.builder()
                        .battle(battle)
                        .status(Status.A)
                        .build();
                battleResultRepository.save(battleResult);
            }

            /*
            try {
                firebaseCloudMessageService.sendMessageTo(battle.getChallenger().getId(),"대결이 종료되었어요.","결과를 확인해 보아요");
                firebaseCloudMessageService.sendMessageTo(battle.getChallenged().getId(),"대결이 종료되었어요.","결과를 확인해 보아요");
            } catch (IOException e) {
                throw new BaseException(BaseResponseStatus.FCM_ERROR);
            }
             */
        }
    }

    public int situation(Long winnerUserId, Long userId) {
        if (winnerUserId == userId) return 0;
        else return 1;
    }

    public BattleSentence getBattleSentence(long battleSentenceId) throws BaseException {
        return battleSentenceRepository.findByIdAndStatus(battleSentenceId, A).orElseThrow(() -> new BaseException(NOT_FOUND_BATTLE_SENTENCE));
    }


    public GetWaitMyBattleInfoResponse getWaitMyBattleInfo(long userId, Long battleIdx) {
        GetWaitMyBattleInfoResponse getWaitMyBattleInfoResponse;
        Battle battle = battleRepository.findByIdAndStatus(battleIdx, BattleStatus.W).orElseThrow(() -> new BaseException(NOT_FOUND_WAIT_BATTLE));
        UserAccount me, opponent;
        if (battle.getChallenger().getId().equals(userId)) {
            me = battle.getChallenger();
            opponent = battle.getChallenged();
        } else {
            me = battle.getChallenged();
            opponent = battle.getChallenger();
        }
        getWaitMyBattleInfoResponse = GetWaitMyBattleInfoResponse.builder()
                .duration(battle.getDuration())
                .userName(me.getNickname())
                .userImage(me.getImage())
                .opponentName(opponent.getNickname())
                .opponentImage(opponent.getImage())
                .word(battle.getWord().getWord().getWord())
                .wordMean(battle.getWord().getWord().getMeaning()).build();
        return getWaitMyBattleInfoResponse;
    }

    public boolean equalsBattleUserId(long userId, Long battleId) {
        Battle battle = battleRepository.findByIdAndStatus(battleId, BattleStatus.W).orElseThrow(() -> new BaseException(INVALID_BATTLE));
        return userId == battle.getChallenged().getId();
    }

    public boolean equalsBattleChallengerId(long userId, Long battleId) {
        Battle battle = battleRepository.findByIdAndStatus(battleId, BattleStatus.W).orElseThrow(() -> new BaseException(INVALID_BATTLE));
        return userId == battle.getChallenger().getId();
    }
}
