package com.umc.pureum.domain.battle;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BattleProvider {
    private final BattleDao battleDao;
}
