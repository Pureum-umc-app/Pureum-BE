package com.umc.pureum.domain.battle;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BattleService {
    private final BattleDao battleDao;
    private final BattleProvider battleProvider;
}
