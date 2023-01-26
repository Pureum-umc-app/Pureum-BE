package com.umc.pureum.domain.user.service;

import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.dto.KakaoAccessTokenInfoDto;
import com.umc.pureum.domain.user.dto.request.CreateUserDto;
import com.umc.pureum.domain.mypage.dto.reponse.GetProfileResponseDto;
import com.umc.pureum.domain.user.dto.response.LogInResponseDto;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.domain.user.entity.mapping.UserProfileMapping;
import com.umc.pureum.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final JwtTokenProvider jwtTokenProvider;
    /**
     * access token으로 유저 정보 가져온 후 회원가입
     *
     * @param kakaoAccessTokenInfoDto // 엑세스 토큰에 담긴 유저 정보
     * @param createUserDto      // 회원가입 할 유저 추가 정보
     */
    public void createUser(KakaoAccessTokenInfoDto kakaoAccessTokenInfoDto, CreateUserDto createUserDto) throws IOException {
        //유저 정보 빌더 하여 저장
        UserAccount userAccount = UserAccount.builder()
                .name(null)
                .email(kakaoAccessTokenInfoDto.has_email ? kakaoAccessTokenInfoDto.getEmail() : null)
                .image(createUserDto.getImage()==null?null : s3Service.uploadFile(createUserDto.getImage()))
                .grade(createUserDto.getGrade())
                .nickname(createUserDto.getNickname())
                .kakaoId(kakaoAccessTokenInfoDto.getId())
                .build();
        userRepository.save(userAccount);
    }

    public boolean validationDuplicateUserNickname(String nickname) {
        return userRepository.existsByNicknameAndStatus(nickname,"A");
    }

    public boolean validationDuplicateKakaoId(Long kakaoId) {
        return userRepository.existsByKakaoIdAndStatus(kakaoId,"A");
    }

    public Long getUserId(Long kakaoId) {
        return userRepository.findByKakaoIdAndStatus(kakaoId,"A").getId();
    }
    public LogInResponseDto userLogIn(Long id) {
        String jwt = jwtTokenProvider.createAccessToken(Long.toString(id));
        return new LogInResponseDto(jwt);
    }

    public GetProfileResponseDto GetProfile(Long id) {
        UserProfileMapping userProfileMapping = userRepository.findUserProfile(id,"A");
        return new GetProfileResponseDto(userProfileMapping);
    }
}
