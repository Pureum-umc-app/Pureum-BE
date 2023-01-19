package com.umc.pureum.domain.use.entity;

import com.umc.pureum.domain.user.entity.User;
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

    @Id @GeneratedValue
    @Column(name = "use_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Time use_time;

    private int count;

    private Time purpose_time;

    @Enumerated(EnumType.STRING)
    private UseStatus purpose_time_status;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    @Enumerated(EnumType.STRING)
    private UseStatus status;

    // 생성 메소드
    @Builder
    public Use(Long id, User user, Time use_time, int count, Time purpose_time, UseStatus purpose_time_status, LocalDateTime created_at, LocalDateTime updated_at, UseStatus status ){
        this.id = id;
        this.user = user;
        this.use_time = use_time;
        this.count = count;
        this.purpose_time = purpose_time;
        this.purpose_time_status = purpose_time_status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
    }

}
