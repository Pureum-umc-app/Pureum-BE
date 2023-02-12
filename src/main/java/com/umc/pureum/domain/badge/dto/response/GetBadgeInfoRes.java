package com.umc.pureum.domain.badge.dto.response;

import java.sql.Timestamp;

public interface GetBadgeInfoRes {
    int getBadge();
    Timestamp getCreatedAt();
}
