package com.umc.pureum.domain.battle.entity;

import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class BattleLike extends BaseEntity {
    @ManyToOne @JoinColumn(name = "battle_sentence_id")
    private BattleSentence sentence;
    @ManyToOne @JoinColumn(name = "user_id")
    private UserAccount user;    private String status;

    @Builder
    public BattleLike(UserAccount user, BattleSentence sentence, String status) {
        this.user = user;
        this.sentence = sentence;
        this.status = status;
    }
}
