package com.umc.pureum.domain.battle.entity;

import com.sun.istack.NotNull;
import com.umc.pureum.domain.sentence.entity.Word;
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
public class Battle extends BaseEntity {
    @ManyToOne @JoinColumn(name = "challenger_id")
    private UserAccount challenger;
    @ManyToOne @JoinColumn(name = "challenged_id")
    private UserAccount challenged;
    @ManyToOne @JoinColumn(name = "battle_word_id")
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
