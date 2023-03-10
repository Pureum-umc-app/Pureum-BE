package com.umc.pureum.domain.sentence.entity;

import com.sun.istack.NotNull;
import com.umc.pureum.global.entity.BaseEntity;
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

public class Keyword extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;
    @OneToMany(mappedBy = "keyword")
    @Builder.Default
    @ToString.Exclude
    private List<Sentence> sentences = new ArrayList<>();
    @NotNull
    private String status;

    @Builder
    public Keyword(Word word, String status) {
        this.word = word;
        this.status = status;
    }
}