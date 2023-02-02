package com.umc.pureum.domain.battle.entity;

import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import com.umc.pureum.global.entity.Status;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class BattleResult extends BaseEntity {
    @OneToOne @JoinColumn(name = "battle_id")
    private Battle battle;
    @ManyToOne @JoinColumn(name = "user_id")
    private UserAccount user;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public BattleResult(Battle battle, UserAccount user, Status status) {
        this.battle = battle;
        this.user = user;
        this.status = status;
    }
}