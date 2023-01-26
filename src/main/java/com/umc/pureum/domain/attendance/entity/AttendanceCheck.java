package com.umc.pureum.domain.attendance.entity;

import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
public class AttendanceCheck extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;
    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;
    
    @Builder
    public AttendanceCheck(UserAccount user, AttendanceStatus status) {
        this.user = user;
        this.status = status;
    }
}
