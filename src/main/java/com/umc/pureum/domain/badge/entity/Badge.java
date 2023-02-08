package com.umc.pureum.domain.badge.entity;


import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Data
@SuperBuilder
@NoArgsConstructor
@DynamicInsert
@Entity
public class Badge extends BaseEntity {

    @ManyToOne @JoinColumn(name = "user_id")
    private UserAccount user;

    private int badge;

    @ColumnDefault("A")
    private String status;

    public Badge(UserAccount user, int badge){
        this.user = user;
        this.badge = badge;
    }
}
