package com.umc.pureum.domain.user.service;

import com.umc.pureum.domain.attendance.AttendanceRepository;
import com.umc.pureum.domain.attendance.entity.AttendanceCheck;
import com.umc.pureum.domain.badge.BadgeRepository;
import com.umc.pureum.domain.badge.entity.Badge;
import com.umc.pureum.domain.battle.entity.Battle;
import com.umc.pureum.domain.battle.repository.BattleRepository;
import com.umc.pureum.domain.notification.FirebaseCloudMessageService;
import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.entity.SentenceLike;
import com.umc.pureum.domain.sentence.repository.SentenceLikeRepository;
import com.umc.pureum.domain.sentence.repository.SentenceRepository;
import com.umc.pureum.domain.use.UseRepository;
import com.umc.pureum.domain.use.entity.UsePhone;
import com.umc.pureum.domain.use.entity.UseStatus;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.dto.request.KakaoAccessTokenInfoDto;
import com.umc.pureum.domain.user.dto.request.CreateUserDto;
import com.umc.pureum.domain.mypage.dto.response.GetProfileResponseDto;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.domain.user.entity.mapping.UserProfileMapping;
import com.umc.pureum.global.config.Response.BaseException;
import com.umc.pureum.global.config.security.jwt.JwtTokenProvider;
import com.umc.pureum.global.entity.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.umc.pureum.global.config.Response.BaseResponseStatus.POST_USERS_NO_EXISTS_USER;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final JwtTokenProvider jwtTokenProvider;
    private final UseRepository useRepository;
    private final AttendanceRepository attendanceRepository;
    private final SentenceRepository sentenceRepository;
    private final SentenceLikeRepository sentenceLikeRepository;
    private final BadgeRepository badgeRepository;
    private final BattleRepository battleRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    /**
     * access token으로 유저 정보 가져온 후 회원가입
     *
     * @param kakaoAccessTokenInfoDto // 엑세스 토큰에 담긴 유저 정보
     * @param createUserDto           // 회원가입 할 유저 추가 정보
     */
    @Transactional
    public void createUser(KakaoAccessTokenInfoDto kakaoAccessTokenInfoDto, CreateUserDto createUserDto) throws IOException {
        //유저 정보 빌더 하여 저장
        UserAccount userAccount = UserAccount.builder()
                .name(null)
                .email(kakaoAccessTokenInfoDto.has_email ? kakaoAccessTokenInfoDto.getEmail() : null)
                .image(createUserDto.getImage() == null ? null : s3Service.uploadFile(createUserDto.getImage()))
                .grade(Integer.parseInt(createUserDto.getGrade()))
                .nickname(createUserDto.getNickname())
                .kakaoId(kakaoAccessTokenInfoDto.getId())
                .build();
        userRepository.save(userAccount);
    }
    public String getNickName(long id){
        Optional<UserAccount> userAccount =  userRepository.findByIdAndStatus(id,"A");
        return userAccount.get().getNickname();
    }
    public boolean validationDuplicateUserNickname(String nickname) {
        return userRepository.existsByNicknameAndStatus(nickname, "A");
    }

    public boolean validationDuplicateUserId(Long id) {
        return userRepository.existsByIdAndStatus(id, "A");
    }

    public boolean validationDuplicateKakaoId(Long kakaoId) {
        return userRepository.existsByKakaoIdAndStatus(kakaoId, "A");
    }

    @Transactional
    public Long getUserId(Long kakaoId) {
        return userRepository.findByKakaoIdAndStatus(kakaoId, "A").getId();
    }

    public String getJwt(Long id) {
        String jwt = jwtTokenProvider.createAccessToken(Long.toString(id));
//        Optional<UserAccount> userAccount = userRepository.findByIdAndStatus(id, "A");
//        userAccount.get().setFcmId(fcmId);
        return jwt;
    }

    public GetProfileResponseDto GetProfile(Long id) {
        UserProfileMapping userProfileMapping = userRepository.findUserProfile(id, "A");
        return new GetProfileResponseDto(userProfileMapping);
    }

    @Transactional
    public void UserResign(long userId) throws BaseException {
        Optional<UserAccount> userAccount = userRepository.findByIdAndStatus(userId, "A");
        List<UsePhone> usePhones = useRepository.findByUserId(userId);
        List<AttendanceCheck> attendanceChecks = attendanceRepository.findByUserId(userId);
        List<Sentence> sentences = sentenceRepository.findByUserId(userId);
        List<SentenceLike> sentenceLikes = sentenceLikeRepository.findByUserId(userId);
        List<Badge> badges = badgeRepository.findByUserId(userId);
        List<Battle> battles = battleRepository.findByChallengerIdOrChallengedId(userId, userId);
        if (userAccount.isPresent()) {
            userAccount.get().setStatus("D");
            userAccount.get().setFcmId(null);
            for (UsePhone usePhone : usePhones) {
                usePhone.setStatus(UseStatus.D);
            }
            for (AttendanceCheck attendanceCheck : attendanceChecks) {
                attendanceCheck.setStatus(Status.D);
            }
            for (Sentence sentence : sentences) {
                sentence.setStatus("D");
                List<SentenceLike> sentenceLikes1 = sentenceLikeRepository.findBySentenceId(sentence.getId());
                for (SentenceLike sentenceLike : sentenceLikes1) {
                    sentenceLike.setStatus(Status.D);
                }
            }
            for (SentenceLike sentenceLike : sentenceLikes) {
                sentenceLike.setStatus(Status.D);
            }
            for (SentenceLike sentenceLike : sentenceLikes) {
                sentenceLike.setStatus(Status.D);
            }
            for (Badge badge : badges) {
                badge.setStatus(Status.D);
            }
//            for (Battle battle : battles) {
//                if (battle.getChallenger().getId() == userId && (battle.getStatus() == BattleStatus.A || battle.getStatus() == BattleStatus.I)) {
//                    battle.setStatus(BattleStatus.D);
//                    try {
//                        firebaseCloudMessageService.sendMessageTo(battle.getChallenged().getId(), "상대가 대결을 취소했어요.", "취소했어요");
//                    } catch (IOException e) {
//                        throw new BaseException(BaseResponseStatus.FCM_ERROR);
//                    }
//                } else if (battle.getChallenged().getId() == userId && (battle.getStatus() == BattleStatus.A || battle.getStatus() == BattleStatus.I)) {
//                    battle.setStatus(BattleStatus.D);
//                    try {
//                        firebaseCloudMessageService.sendMessageTo(battle.getChallenger().getId(), "상대가 대결을 취소했어요.", "취소했어요");
//                    } catch (IOException e) {
//                        throw new BaseException(BaseResponseStatus.FCM_ERROR);
//                    }
//                }
//            }
        } else
            throw new BaseException(POST_USERS_NO_EXISTS_USER);
    }

    public UserAccount getUser(long userId) throws BaseException {
        return userRepository.findByIdAndStatus(userId, "A").orElseThrow(() -> new BaseException(POST_USERS_NO_EXISTS_USER));
    }
}