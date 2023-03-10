package com.umc.pureum.domain.battle.entity;

import com.umc.pureum.domain.sentence.entity.SentenceLike;
import com.umc.pureum.domain.sentence.entity.Word;
import com.umc.pureum.global.entity.BaseEntity;
import com.umc.pureum.global.entity.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)

public class BattleWord extends BaseEntity {
    @OneToOne @JoinColumn(name = "word_id")
    private Word word;
    @OneToMany(mappedBy = "word")
    @Builder.Default
    @ToString.Exclude
    private List<BattleSentence> battleSentences = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public BattleWord(Word word, Status status) {
        this.word = word;
        this.status = status;
    }
}