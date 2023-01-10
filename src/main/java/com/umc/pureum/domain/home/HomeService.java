package com.umc.pureum.domain.home;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HomeService {
    private final HomeDao homeDao;
}
