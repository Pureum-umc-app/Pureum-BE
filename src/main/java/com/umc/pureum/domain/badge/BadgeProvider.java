package com.umc.pureum.domain.badge;


import com.umc.pureum.domain.badge.entity.Badge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BadgeProvider {

    private final BadgeDao badgeDao;


    // 배지 있는 지 조회
    public Boolean inquireBadge(Long userId, int badge) {
        Optional<Badge> badgeOne = badgeDao.findBadge(userId, badge);
        return ObjectUtils.isEmpty(badgeOne);
    }
}
