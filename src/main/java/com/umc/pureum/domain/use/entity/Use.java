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
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Use extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    private Time use_time;

    private int count;

    private Time purpose_time;

    @Enumerated(EnumType.STRING)
    private UseStatus status;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    // use 정보 업데이트
    @Builder
    public Use(UserAccount user, Time use_time, int count, Time purpose_time, LocalDateTime created_at, LocalDateTime updated_at, UseStatus status ){
        this.user = user;
        this.use_time = use_time;
        this.count = count;
        this.purpose_time = purpose_time;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
    }

}
