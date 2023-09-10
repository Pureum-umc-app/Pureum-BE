package com.umc.pureum.domain.battle.entity;

import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)

public class Battle extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenger_id")
    private UserAccount challenger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenged_id")
    private UserAccount challenged;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battle_word_id")
    private BattleWord word;
    private int duration;
    @Enumerated(EnumType.STRING)
    private BattleStatus status;

    @Builder
    public Battle(UserAccount challenger, UserAccount challenged, BattleWord word, int duration, BattleStatus status) {
        this.challenger = challenger;
        this.challenged = challenged;
        this.word = word;
        this.duration = duration;
        this.status = status;
    }
}