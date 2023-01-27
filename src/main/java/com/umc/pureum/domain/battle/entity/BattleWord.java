package com.umc.pureum.domain.battle.entity;

import com.sun.istack.NotNull;
import com.umc.pureum.domain.sentence.entity.Word;
import com.umc.pureum.global.entity.BaseEntity;
import com.umc.pureum.global.entity.Status;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class BattleWord extends BaseEntity {
    @OneToOne @JoinColumn(name = "word_id")
    private Word word;
    @NotNull
    private Status status;

    @Builder
    public BattleWord(Word word, Status status) {
        this.word = word;
        this.status = status;
    }
}
