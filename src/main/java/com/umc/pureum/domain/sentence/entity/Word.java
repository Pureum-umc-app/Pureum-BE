package com.umc.pureum.domain.sentence.entity;

import com.sun.istack.NotNull;
import com.umc.pureum.global.entity.BaseEntity;
import com.umc.pureum.global.entity.Status;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)

public class Word extends BaseEntity {
    @NotNull
    private String word;
    private String meaning;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Word(String word, String meaning, Status status) {
        this.word = word;
        this.meaning = meaning;
        this.status = status;
    }
}
