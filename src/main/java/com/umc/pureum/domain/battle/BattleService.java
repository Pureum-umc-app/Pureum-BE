package com.umc.pureum.domain.battle;

import com.umc.pureum.domain.battle.dao.BattleDao;
import com.umc.pureum.domain.battle.dao.BattleLikeDao;
import com.umc.pureum.domain.battle.dao.BattleSentenceDao;
import com.umc.pureum.domain.battle.dto.repsonse.ReturnFinishBattleRes;
import com.umc.pureum.domain.battle.dto.repsonse.*;
import com.umc.pureum.domain.battle.dto.request.BattleStatusReq;
import com.umc.pureum.domain.battle.dto.request.CreateChallengedSentenceReq;
import com.umc.pureum.domain.battle.dto.request.LikeBattleReq;
import com.umc.pureum.domain.battle.dto.request.PostBattleReq;
import com.umc.pureum.domain.battle.entity.*;
import com.umc.pureum.domain.battle.repository.*;
import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Word;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponseStatus;
import com.umc.pureum.global.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.umc.pureum.global.config.BaseResponseStatus.*;
import static com.umc.pureum.global.entity.Status.A;

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

    // accept : 대결 수락
    @Transactional
    public BattleStatusRes accept(BattleStatusReq request) {

        // request 로 받은 battleId 로 battle 찾기
        Battle battle = battleDao.findOne(request.getBattleId());

        // battle 상태를 수락 완료로 바꾸기
        battle.setStatus(BattleStatus.A);

        // battle ID , Status 값
        return new BattleStatusRes(battle.getId(), battle.getStatus());
    }

    // reject : 대결 거절
    @Transactional
    public BattleStatusRes reject(BattleStatusReq request) {

        // request 로 받은 battleId 로 battle 찾기
        Battle battle = battleDao.findOne(request.getBattleId());

        // battle 상태를 수락 완료로 바꾸기
        battle.setStatus(BattleStatus.D);

        // battle ID , Status 값
        return new BattleStatusRes(battle.getId(), battle.getStatus());
    }

    // cancel : 대결 취소
    @Transactional
    public BattleStatusRes cancel(BattleStatusReq request) {

        // request 로 받은 battleId 로 battle 찾기
        Battle battle = battleDao.findOne(request.getBattleId());

        // battle 상태를 수락 완료로 바꾸기
        battle.setStatus(BattleStatus.D);

        // battle ID , Status 값
        return new BattleStatusRes(battle.getId(), battle.getStatus());
    }

    /* 대결 신청 API */
    @Transactional
    public Long createBattle (PostBattleReq postBattleReq) throws BaseException {
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
        if (word.isEmpty()) {
            throw new BaseException(BaseResponseStatus.POST_BATTLE_NO_EXIST_KEYWORD);
        }
        Optional<Battle> battle = battleRepository.findByUserIdAndWordId(postBattleReq.getChallengerId(), postBattleReq.getWordId());
        if (battle.isPresent()) {
            throw new BaseException(BaseResponseStatus.POST_BATTLE_ALREADY_EXIST_KEYWORD);
        }

        // 대결 저장
        Battle newBattle = new Battle(challenger.get(), challenged.get(), word.get(), postBattleReq.getDuration(), BattleStatus.W);
        Battle savedBattle = battleRepository.save(newBattle);

        // 문장 저장
        BattleSentence sentence = new BattleSentence(savedBattle, challenger.get(),
                postBattleReq.getSentence(), word.get(), Status.A);
        battleSentenceRepository.save(sentence);

        return savedBattle.getId();
    }

    public String BattleMyProfilePhoto ( long userId){
        return userRepository.findByIdAndStatus(userId, "A").get().getImage();

    }

    // writeChallenged : challenged 의 문장 작성 DB 에 저장
    @Transactional
    public CreateChallengedSentenceRes writeChallenged (Long userId, CreateChallengedSentenceReq request) throws BaseException {

        Long battleId = request.getBattleId();
        String writingSentence = request.getSentence();

        // request 로 받은 battleId 로 배틀 찾기
        Battle battle = battleDao.findOne(battleId);

        // battle 상태 바꾸기
        battle.setStatus(BattleStatus.I);

        // request 로 받은 battleWordId 로 단어 찾기
        BattleWord battleWord = battle.getWord();
        Word word = battleWord.getWord();
        String writingWord = word.getWord();

        // 작성한 문장 존재 여부 확인
        if (writingSentence == "") {
            throw new BaseException(POST_SENTENCE_EMPTY);
        }

        // 작성할 문장에 단어 포함 여부 확인
        else if (!isExist(writingSentence, writingWord)) {
            throw new BaseException(POST_SENTENCE_NO_EXISTS_KEYWORD);
        }

        // request 로 받은 userId 로 userAccount 찾기
        UserAccount userAccount = userRepository.findById(userId).get();

        BattleSentence battleSentence = new BattleSentence(battle, userAccount, writingSentence, battleWord, Status.A);
        battleSentenceDao.save(battleSentence);

        return new CreateChallengedSentenceRes(battleSentence.getId(), battle.getId(), battle.getStatus());
    }


    /* Sentence 내에 Keyword 존재여부 검사*/
    // isExist : 문장에 키워드가 포함되어있는지 확인하는 함수
    private boolean isExist (String writingSentence, String writingWord){
        return writingSentence.contains(writingWord);
    }

    // like : 대결 좋아요 DB 에 저장
    @Transactional
    public LikeBattleRes like(long userId , LikeBattleReq request) {

        // request 로 받은 sentenceId 로 문장 찾기
        BattleSentence battleSentence = battleSentenceDao.findOne(request.getSentenceId());

        // request 로 받은 userId 로 userAccount 찾기
        UserAccount userAccount = userRepository.findById(userId).get();

        //request 로 받은 sentenceId 로 문장 좋아요 찾기
        if(battleLikeDao.findBySentenceId(request.getSentenceId()).isPresent()){

            BattleLike battleLike = battleLikeDao.findBySentenceId(request.getSentenceId()).get();

            // 존재하는 sentence 일 경우 sentence status 확인하고 status 바꾼다 .
            if(Status.A.equals(battleLike.getStatus())){
                battleLike.setStatus(Status.D);
            }
            else if(Status.D.equals(battleLike.getStatus())){
                battleLike.setStatus(Status.A);
            }

            return new LikeBattleRes(battleLike.getId(),battleLike.getStatus());

        }

        // 존재하지 않는 sentence 일 경우 sentenceLike 생성해서 저장
        else{
            BattleLike battleLike = new BattleLike(userAccount, battleSentence, Status.A);
            battleLikeDao.save(battleLike);

            return new LikeBattleRes(battleLike.getId(),battleLike.getStatus());
        }

    }


    //returnRunBattle : 대결 정보 return
    @Transactional
    public ReturnRunBattleRes returnRunBattle(long battleIdx , Long userId) throws BaseException {
        GetBattleInfoRes battleInfo = battleRepository.findInfoByBattleId(battleIdx).stream().findAny().get();

        if (battleInfo.getBattleStatus().equals(BattleStatus.W) || battleInfo.getBattleStatus().equals(BattleStatus.A) || battleInfo.getBattleStatus().equals(BattleStatus.I)) {

            Long challengedId = battleInfo.getChallengedId();
            Long challengerId = battleInfo.getChallengerId();

            GetBattleSentenceInterface challengedSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengedId).stream().findAny().get();
            GetBattleSentenceInterface challengerSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengerId).stream().findAny().get();

            Long challengedSentenceId = challengedSentenceInfo.getBattleSentenceId();
            Long challengerSentenceId = challengerSentenceInfo.getBattleSentenceId();

            GetBattleLikeInterface challengedLikeInterface = likeRepository.findByUserId(challengedId, challengedSentenceId).stream().findAny().get();
            GetBattleLikeInterface challengerLikeInterface = likeRepository.findByUserId(challengerId, challengerSentenceId).stream().findAny().get();

            if (userId == challengedId) {
                return new ReturnRunBattleRes(battleInfo.getBattleId(), battleInfo.getKeywordId(), battleInfo.getKeyword(),
                        battleInfo.getChallengedId(), battleInfo.getChallengedNickname(), battleInfo.getChallengedProfileImg(),
                        battleInfo.getChallengerId(), battleInfo.getChallengerNickname(), battleInfo.getChallengerProfileImg(),
                        battleInfo.getDuration(), battleInfo.getBattleStatus(),
                        challengedSentenceInfo.getBattleSentenceId(), challengedSentenceInfo.getBattleSentence(),
                        challengerSentenceInfo.getBattleSentenceId(), challengerSentenceInfo.getBattleSentence(),
                        challengedLikeInterface.getLikeCnt(), challengerLikeInterface.getLikeCnt(),
                        challengedLikeInterface.getIsLike()
                );
            } else if (userId == challengerId) {
                return new ReturnRunBattleRes(battleInfo.getBattleId(), battleInfo.getKeywordId(), battleInfo.getKeyword(),
                        battleInfo.getChallengedId(), battleInfo.getChallengedNickname(), battleInfo.getChallengedProfileImg(),
                        battleInfo.getChallengerId(), battleInfo.getChallengerNickname(), battleInfo.getChallengerProfileImg(),
                        battleInfo.getDuration(), battleInfo.getBattleStatus(),
                        challengedSentenceInfo.getBattleSentenceId(), challengedSentenceInfo.getBattleSentence(),
                        challengerSentenceInfo.getBattleSentenceId(), challengerSentenceInfo.getBattleSentence(),
                        challengedLikeInterface.getLikeCnt(), challengerLikeInterface.getLikeCnt(),
                        challengerLikeInterface.getIsLike()
                );
            }

        } else {
            throw new BaseException(GET_BATTLE_FINISH_STATUS);
        }
        return null;
    }

    //returnRunBattle : 대결 정보 return
    @Transactional
    public ReturnFinishBattleRes returnFinishBattle(long battleIdx , Long userId) throws BaseException {
        GetBattleInfoRes battleInfo = battleRepository.findInfoByBattleId(battleIdx).stream().findAny().get();

        if (battleInfo.getBattleStatus().equals(BattleStatus.D) || battleInfo.getBattleStatus().equals(BattleStatus.D)) {

            GetBattleResultInterface getBattleResultInterface = battleResultRepository.findBattleResultByBattleId(battleIdx).stream().findAny().get();

            System.out.println("!!!!!!!!!!!!!!!!"+getBattleResultInterface.getUserId());

            Long challengedId = battleInfo.getChallengedId();
            Long challengerId = battleInfo.getChallengerId();

            GetBattleSentenceInterface challengedSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengedId).stream().findAny().get();
            GetBattleSentenceInterface challengerSentenceInfo = battleSentenceRepository.findInfoByBattleIdAndUserId(battleIdx, challengerId).stream().findAny().get();

            Long challengedSentenceId = challengedSentenceInfo.getBattleSentenceId();
            Long challengerSentenceId = challengerSentenceInfo.getBattleSentenceId();

            GetBattleLikeInterface challengedLikeInterface = likeRepository.findByUserId(challengedId, challengedSentenceId).stream().findAny().get();
            GetBattleLikeInterface challengerLikeInterface = likeRepository.findByUserId(challengerId, challengerSentenceId).stream().findAny().get();

            if (userId == challengedId) {
                return new ReturnFinishBattleRes(battleIdx , getBattleResultInterface.getUserId(),
                        battleInfo.getChallengedId(), battleInfo.getChallengedNickname(), battleInfo.getChallengedProfileImg(),
                        battleInfo.getChallengerId(), battleInfo.getChallengerNickname(), battleInfo.getChallengerProfileImg(),
                        challengedSentenceInfo.getBattleSentenceId(), challengedSentenceInfo.getBattleSentence(),
                        challengerSentenceInfo.getBattleSentenceId(), challengerSentenceInfo.getBattleSentence(),
                        challengedLikeInterface.getLikeCnt(), challengerLikeInterface.getLikeCnt(),
                        challengerLikeInterface.getIsLike()
                        );
            } else if (userId == challengerId) {
                return new ReturnFinishBattleRes(battleIdx , getBattleResultInterface.getUserId(),
                        battleInfo.getChallengedId(), battleInfo.getChallengedNickname(), battleInfo.getChallengedProfileImg(),
                        battleInfo.getChallengerId(), battleInfo.getChallengerNickname(), battleInfo.getChallengerProfileImg(),
                        challengedSentenceInfo.getBattleSentenceId(), challengedSentenceInfo.getBattleSentence(),
                        challengerSentenceInfo.getBattleSentenceId(), challengerSentenceInfo.getBattleSentence(),
                        challengedLikeInterface.getLikeCnt(), challengerLikeInterface.getLikeCnt(),
                        challengerLikeInterface.getIsLike()
                );
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

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void setResult() {
        List<Battle> battleIdList = battleRepository.findByEndBattle();
        long challengedBattleSentenceId;
        long challengerBattleSentenceId;
        int challengerLikeNum;
        int challengedLikeNum;
        BattleResult battleResult;
        for (Battle battle : battleIdList) {
            challengedBattleSentenceId = battleSentenceRepository.findIdByBattleIdAndUserIdAndStatus(battle.getId(), battle.getChallenged().getId());
            challengerBattleSentenceId = battleSentenceRepository.findIdByBattleIdAndUserIdAndStatus(battle.getId(), battle.getChallenger().getId());
            challengedLikeNum = battleLikeRepository.CountLikeNumBySentenceId(challengedBattleSentenceId);
            challengerLikeNum = battleLikeRepository.CountLikeNumBySentenceId(challengerBattleSentenceId);
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
        }
    }
}
