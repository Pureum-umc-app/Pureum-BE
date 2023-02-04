package com.umc.pureum.domain.use.entity;

import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
@DynamicInsert
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UsePhone extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    private Time useTime;

    private int count;

    private Time purposeTime;

    @Enumerated(EnumType.STRING)
    private UseStatus status;

    public UsePhone(UserAccount user, Time useTime, int count){
        this.user = user;
        this.useTime = useTime;
        this.count = count;
    }

    public void updateUsePhone(Time useTime, int count){
        this.useTime = useTime;
        this.count = count;
    }
}
