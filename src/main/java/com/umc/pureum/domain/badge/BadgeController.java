package com.umc.pureum.domain.badge;


import com.umc.pureum.domain.badge.dto.SaveBadgeReq;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import static com.umc.pureum.global.config.BaseResponseStatus.*;

@Api(tags = "배지")
@RequiredArgsConstructor
@RestController
@RequestMapping("/badges")
public class BadgeController {

    private final BadgeProvider badgeProvider;
    private final BadgeService badgeService;

    @ApiOperation("배지 저장 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
            @ApiImplicitParam(name = "userId", paramType = "path", value = "유저 인덱스", example = "1", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "saveBadgeReq", paramType = "body", value = "배지(int)", example = "0", dataTypeClass = SaveBadgeReq.class),
    })
    @PostMapping("/{userId}")
    public BaseResponse<String> saveBadge(@PathVariable Long userId, @RequestBody SaveBadgeReq saveBadgeReq) {
        // springSecurity 에서 userId 받아와서 Long 형으로 바꿈
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String springSecurityUserId = principal.getUsername();
        Long userIdSC = Long.parseLong(springSecurityUserId);
        if (userId != userIdSC) {
            return new BaseResponse<>(INVALID_JWT);
        } else {
            if (badgeProvider.inquireBadge(userIdSC, saveBadgeReq.getBadge())) { // 배지가 존재하는지 확인
                badgeService.saveBadge(userIdSC, saveBadgeReq.getBadge());
                return new BaseResponse<>(SUCCESS);
            } else {
                return new BaseResponse<>(POST_BADGE_EXITS);
            }
        }
    }
}
