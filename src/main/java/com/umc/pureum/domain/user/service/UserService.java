package com.umc.pureum.domain.user.service;

import com.umc.pureum.domain.attendance.AttendanceRepository;
import com.umc.pureum.domain.attendance.entity.AttendanceCheck;
import com.umc.pureum.domain.attendance.entity.AttendanceStatus;
import com.umc.pureum.domain.badge.BadgeRepository;
import com.umc.pureum.domain.badge.entity.Badge;
import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.entity.SentenceLike;
import com.umc.pureum.domain.sentence.repository.SentenceLikeRepository;
import com.umc.pureum.domain.sentence.repository.SentenceRepository;
import com.umc.pureum.domain.use.UseRepository;
import com.umc.pureum.domain.use.entity.UsePhone;
import com.umc.pureum.domain.use.entity.UseStatus;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.dto.request.FCMDto;
import com.umc.pureum.domain.user.dto.request.KakaoAccessTokenInfoDto;
import com.umc.pureum.domain.user.dto.request.CreateUserDto;
import com.umc.pureum.domain.mypage.dto.response.GetProfileResponseDto;
import com.umc.pureum.domain.user.dto.response.LogInResponseDto;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.domain.user.entity.mapping.UserProfileMapping;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.umc.pureum.global.config.BaseResponseStatus.POST_USERS_NO_EXISTS_USER;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoService kakaoService;
    private final UseRepository useRepository;
    private final AttendanceRepository attendanceRepository;
    private final SentenceRepository sentenceRepository;
    private final SentenceLikeRepository sentenceLikeRepository;
    private final BadgeRepository badgeRepository;

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
                .grade(createUserDto.getGrade())
                .nickname(createUserDto.getNickname())
                .kakaoId(kakaoAccessTokenInfoDto.getId())
                .build();
        userRepository.save(userAccount);
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

    public LogInResponseDto userLogIn(Long id, FCMDto fcmDto) {
        String jwt = jwtTokenProvider.createAccessToken(Long.toString(id));
        Optional<UserAccount> userAccount = userRepository.findByIdAndStatus(id, "A");
        userAccount.get().setFcmId(fcmDto.getFcmId());
        return new LogInResponseDto(jwt);
    }

    public GetProfileResponseDto GetProfile(Long id) {
        UserProfileMapping userProfileMapping = userRepository.findUserProfile(id, "A");
        return new GetProfileResponseDto(userProfileMapping);
    }

    @Transactional
    public void UserResign(long userId, String accessToken) throws BaseException {
        Optional<UserAccount> userAccount = userRepository.findByIdAndStatus(userId, "A");
        List<UsePhone> usePhones = useRepository.findByUserId(userId);
        List<AttendanceCheck> attendanceChecks = attendanceRepository.findByUserId(userId);
        List<Sentence> sentences = sentenceRepository.findByUserId(userId);
        List<SentenceLike> sentenceLikes = sentenceLikeRepository.findByUserId(userId);
        List<Badge> badges = badgeRepository.findByUserId(userId);
        if (userAccount.isPresent()) {
            userAccount.get().setStatus("D");
            for (UsePhone usePhone : usePhones) {
                usePhone.setStatus(UseStatus.D);
            }
            for (AttendanceCheck attendanceCheck : attendanceChecks) {
                attendanceCheck.setStatus(AttendanceStatus.D);
            }
            for (Sentence sentence : sentences) {
                sentence.setStatus("D");
                List<SentenceLike> sentenceLikes1 = sentenceLikeRepository.findBySentenceId(sentence.getId());
                for (SentenceLike sentenceLike : sentenceLikes1) {
                    sentenceLike.setStatus("D");
                }
            }
            for (SentenceLike sentenceLike : sentenceLikes) {
                sentenceLike.setStatus("D");
            }
            for (SentenceLike sentenceLike : sentenceLikes) {
                sentenceLike.setStatus("D");
            }
            for (Badge badge : badges) {
                badge.setStatus("D");
            }
        } else
            throw new BaseException(POST_USERS_NO_EXISTS_USER);
        kakaoService.unlink(accessToken);
    }
}