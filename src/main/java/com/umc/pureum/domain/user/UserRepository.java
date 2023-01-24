package com.umc.pureum.domain.user;

import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserAccount, Long> {

    boolean existsByNickname(String nickname);
    boolean existsByKakaoId(Long kakaoId);

    UserAccount findByKakaoId(Long id);
}
