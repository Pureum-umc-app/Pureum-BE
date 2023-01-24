package com.umc.pureum.domain.user;

import com.umc.pureum.domain.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserAccount, Long> {
    boolean existsByNickname(String nickname);
    boolean existsByKakaoId(Long kakaoId);
    UserAccount findByKakaoId(Long id);
    Optional<UserAccount> findByIdAndStatus(Long id, String status);

    boolean existsByNickname(String nickname);
    boolean existsByKakaoId(Long kakaoId);

    UserAccount findByKakaoId(Long id);
}
