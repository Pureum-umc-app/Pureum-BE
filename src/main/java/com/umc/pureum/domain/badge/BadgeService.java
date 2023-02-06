package com.umc.pureum.domain.badge;


import com.umc.pureum.domain.badge.entity.Badge;
import com.umc.pureum.domain.user.UserDao;
import com.umc.pureum.domain.user.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BadgeService {

    private final BadgeDao badgeDao;
    private final UserDao userDao;

    // 배지 저장
    @Transactional
    public void saveBadge(Long userId, int badge){
        UserAccount userAccount = userDao.find(userId);
        Badge badgeOne = new Badge(userAccount, badge);
        badgeDao.save(badgeOne);
    }
}
