package com.umc.pureum.domain.battle.entity;

import com.umc.pureum.domain.sentence.entity.Sentence;
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
public class BattleLike extends BaseEntity {
    @ManyToOne @JoinColumn(name = "battle_sentence_id")
    private BattleSentence sentence;
    @ManyToOne @JoinColumn(name = "user_id")
    private UserAccount user;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public BattleLike(UserAccount user, BattleSentence sentence, Status status) {
        this.user = user;
        this.sentence = sentence;
        this.status = status;
    }
}