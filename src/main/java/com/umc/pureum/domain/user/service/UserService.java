package com.umc.pureum.domain.user.service;

import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.dto.AccessTokenInfoDto;
import com.umc.pureum.domain.user.dto.request.CreateUserDto;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final KakaoService kakaoService;

    /**
     * access token으로 유저 정보 가져온 후 회원가입
     * @param accessToken // 엑세스 토큰
     * @param createUserDto // 회원가입 할 유저 추가 정보
     * @throws BaseException
     */
    public void createUser(String accessToken, CreateUserDto createUserDto) {
        //accessToken로 user 정보 가져오기
        AccessTokenInfoDto accessTokenInfoDto = kakaoService.getUserInfoByKakaoToken(accessToken);
        //유저 정보 빌더 하여 저장
        UserAccount userAccount = UserAccount.builder()
                .name(null)
                .id(accessTokenInfoDto.getId())
                .email(accessTokenInfoDto.has_email ? accessTokenInfoDto.getEmail() : null)
                .image(createUserDto.getProfile_photo_url())
                .grade(createUserDto.getGrade())
                .nickname(createUserDto.getNickname())
                .kakaoId(accessTokenInfoDto.getId())
                .build();
            userRepository.save(userAccount);
    }
    public boolean validationDuplicateUserNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
