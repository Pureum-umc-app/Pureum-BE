package com.umc.pureum.global.config.security.jwt;


import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
//DB에 접근해서 유저가 존재하는지 확인하는 부분
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<UserAccount> userAccount = userRepository.findById(Long.parseLong(username));
        //유저 ID 값
        return User.withUsername(username)
                //유저 비밀번호(우리는 비밀번호가 없어서 일단 image 주소값을 넣었습니다. 나중에 자체 로그인구현하게되면 이부분 수정
                .password(userAccount.get().getImage())
                //우리는 권한설정을 따로 하지 않았기에 NO_AUTHORITIES를 사용
                .authorities(AuthorityUtils.NO_AUTHORITIES)
                .build();
    }
}