package com.umc.pureum.domain.attendance.entity;

import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import com.umc.pureum.global.entity.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

public class AttendanceCheck extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public AttendanceCheck(UserAccount user, Status status) {
        this.user = user;
        this.status = status;
    }
}
