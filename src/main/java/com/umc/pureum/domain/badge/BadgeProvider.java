package com.umc.pureum.domain.badge;


import com.umc.pureum.domain.badge.entity.Badge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BadgeProvider {

    private final BadgeRepository badgeRepository;


    // 배지 있는 지 조회
    public Boolean inquireBadge(Long userId, int badge) {
        Optional<Badge> badgeOne = badgeRepository.findByUserIdAndBadge(userId, badge)
                .stream().findAny();
        return badgeOne.isEmpty();
    }
}
