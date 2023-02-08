package com.umc.pureum.domain.badge;


import com.umc.pureum.domain.badge.dto.response.GetBadgeInfoRes;
import com.umc.pureum.domain.badge.dto.response.ReturnBadgesRes;
import com.umc.pureum.domain.badge.entity.Badge;
import com.umc.pureum.domain.user.UserDao;
import com.umc.pureum.domain.user.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BadgeService {

    private final BadgeDao badgeDao;
    private final UserDao userDao;
    private final BadgeRepository badgeRepository;

    // 배지 저장
    @Transactional
    public void saveBadge(Long userId, int badge){
        UserAccount userAccount = userDao.find(userId);
        Badge badgeOne = new Badge(userAccount, badge);
        badgeDao.save(badgeOne);
    }

    // getBadges : 배지 값 return
    @Transactional
    public ReturnBadgesRes getBadges(Long userId){
        int[] badges = new int[10];

        Timestamp timestamp = null;

        List<GetBadgeInfoRes> badgesByUserId = badgeRepository.findBadgesByUserId(userId);

        System.out.println(badgesByUserId.get(0).getBadge());

        for( int i = 0 ; i < badgesByUserId.size() ; i++ ){
            GetBadgeInfoRes getBadgeInfoRes = badgesByUserId.get(i);

            System.out.println(getBadgeInfoRes.getBadge());
            System.out.println("@@@@@@@@@@@@@@@"+getBadgeInfoRes.getCreatedAt());

            if( i == 0 ){
                timestamp = getBadgeInfoRes.getCreatedAt();
            }

            if( (getBadgeInfoRes.getCreatedAt().getTime() - timestamp.getTime()) > 0){
                timestamp = getBadgeInfoRes.getCreatedAt();
                badges[0] = getBadgeInfoRes.getBadge();
            }

            badges[getBadgeInfoRes.getBadge()] = 1;
        }

        return new ReturnBadgesRes(Arrays.stream(badges).boxed().collect(Collectors.toList()) , badgeRepository.findBadgesByUserId(userId).size());

    }
}
