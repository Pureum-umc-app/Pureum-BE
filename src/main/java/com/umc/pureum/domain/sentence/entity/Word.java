package com.umc.pureum.domain.sentence.entity;

import com.sun.istack.NotNull;
import com.umc.pureum.global.entity.BaseEntity;
import com.umc.pureum.global.entity.Status;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class Word extends BaseEntity {
    @NotNull
    private String word;
    @NotNull
    private String meaning;
    @NotNull
    private Status status;

    @Builder
    public Word(String word, String meaning, Status status) {
        this.word = word;
        this.meaning = meaning;
        this.status = status;
    }
}
