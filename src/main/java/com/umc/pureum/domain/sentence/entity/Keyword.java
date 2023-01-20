package com.umc.pureum.domain.sentence.entity;

import com.sun.istack.NotNull;
import com.umc.pureum.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class Keyword extends BaseEntity {
    @NotNull
    private String word;
    @NotNull
    private String status;

    @Builder
    public Keyword(String word, String status) {
        this.word = word;
        this.status = status;
    }
}
