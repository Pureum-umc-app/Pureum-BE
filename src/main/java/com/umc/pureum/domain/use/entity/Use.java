package com.umc.pureum.domain.use.entity;

import com.umc.pureum.domain.user.entity.User;
import com.umc.pureum.global.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Use extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "use_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Time use_time;

    private int count;

    @Enumerated(EnumType.STRING)
    private UseStatus purpose_time_status;

    private Time purpose_time;

    @Enumerated(EnumType.STRING)
    private UseStatus status;

}
