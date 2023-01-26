package com.umc.pureum.domain.use.entity;

import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
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

}
