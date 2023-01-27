package com.umc.pureum.domain.battle.entity;

import com.umc.pureum.domain.sentence.entity.Word;
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
public class BattleWord extends BaseEntity {
    @OneToOne @JoinColumn(name = "word_id")
    private Word word;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public BattleWord(Word word, Status status) {
        this.word = word;
        this.status = status;
    }
}
