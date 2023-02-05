package com.umc.pureum.domain.badge.entity;


import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class Badge extends BaseEntity {

    @ManyToOne @JoinColumn(name = "user_id")
    private UserAccount user;

    private int badge;

    private String status;

    public Badge(UserAccount user, int badge){
        this.user = user;
        this.badge = badge;
    }
}
