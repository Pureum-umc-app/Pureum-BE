package com.umc.pureum.domain.sentence.entity;

import com.sun.istack.NotNull;
import com.umc.pureum.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)

public class Keyword extends BaseEntity {
    @OneToOne @JoinColumn(name = "word_id")
    private Word word;
    @NotNull
    private String status;

    @Builder
    public Keyword(Word word, String status) {
        this.word = word;
        this.status = status;
    }
}