package com.umc.pureum.domain.blame.entity;

import com.umc.pureum.domain.battle.entity.BattleSentence;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Getter
@Entity
@Table(name = "battle_sentence_blame")
public class BattleSentenceBlame extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battle_sentence_id")
    BattleSentence battleSentence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    UserAccount user;
    @Enumerated(EnumType.STRING)
    Status status;

    public void updateState(Status state) {
        this.status = state;
    }

    public void setUser(UserAccount userAccount) {
        this.user = userAccount;
    }

    public void setBattleSentence(BattleSentence battleSentence) {
        this.battleSentence = battleSentence;
    }

    public enum Status {
        A,D
    }
}
