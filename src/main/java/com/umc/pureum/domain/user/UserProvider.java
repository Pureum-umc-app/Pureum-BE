package com.umc.pureum.domain.user;

import com.umc.pureum.global.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserProvider {
    private final UserDao userDao;
    private final JwtService jwtService;
}
