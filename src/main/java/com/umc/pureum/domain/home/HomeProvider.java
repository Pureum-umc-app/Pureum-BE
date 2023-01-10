package com.umc.pureum.domain.home;

import com.umc.pureum.global.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HomeProvider {
    private final HomeDao homeDao;
    private final JwtService jwtService;
}
