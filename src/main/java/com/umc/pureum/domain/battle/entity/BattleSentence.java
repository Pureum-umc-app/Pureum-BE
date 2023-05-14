package com.umc.pureum.domain.battle.entity;

import com.umc.pureum.domain.blame.entity.BattleSentenceBlame;
import com.umc.pureum.domain.use.entity.UsePhone;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import com.umc.pureum.global.entity.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)

public class BattleSentence extends BaseEntity {
    @OneToOne @JoinColumn(name = "battle_id")
    private Battle battle;
    @ManyToOne @JoinColumn(name = "user_id")
    private UserAccount user;
    @NotNull
    private String sentence;
    @ManyToOne @JoinColumn(name = "battle_word_id")
    private BattleWord word;

    @OneToMany(mappedBy = "sentence")
    @Builder.Default
    @ToString.Exclude
    private List<BattleLike> battleLikes = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany(mappedBy = "battleSentence")
    @Builder.Default
    @ToString.Exclude
    private List<BattleSentenceBlame> battleSentenceBlameList = new ArrayList<>();
    @Builder
    public BattleSentence(Battle battle, UserAccount user, @NotNull String sentence, BattleWord word, Status status) {
        this.battle = battle;
        this.user = user;
        this.sentence = sentence;
        this.word = word;
        this.status = status;
    }


    public void addBattleSentenceBlame(BattleSentenceBlame battleSentenceBlame) {
        battleSentenceBlameList.add(battleSentenceBlame);
        battleSentenceBlame.setBattleSentence(this);
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}