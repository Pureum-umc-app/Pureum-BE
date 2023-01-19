package com.umc.pureum.domain.use.entity;

import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class UsePhone extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    private Time useTime;

    private int count;

    private Time purposeTime;

    @Enumerated(EnumType.STRING)
    private UseStatus status;

    // use 정보 업데이트
    @Builder
    public UsePhone(UserAccount user, Time use_time, int count, Time purpose_time, UseStatus status ){
        this.user = user;
        this.useTime = use_time;
        this.count = count;
        this.purposeTime = purpose_time;
        this.status = status;
    }

}
