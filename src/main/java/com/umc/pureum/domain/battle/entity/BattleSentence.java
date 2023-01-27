package com.umc.pureum.domain.battle.entity;

import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import com.umc.pureum.global.entity.Status;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class BattleSentence extends BaseEntity {
    @OneToOne @JoinColumn(name = "battle_id")
    private Battle battle;
    @ManyToOne @JoinColumn(name = "user_id")
    private UserAccount user;
    @NotNull
    private String sentence;
    @ManyToOne @JoinColumn(name = "battle_word_id")
    private BattleWord word;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public BattleSentence(Battle battle, UserAccount user, String sentence, BattleWord word, Status status) {
        this.battle = battle;
        this.user = user;
        this.sentence = sentence;
        this.word = word;
        this.status = status;
    }
}
