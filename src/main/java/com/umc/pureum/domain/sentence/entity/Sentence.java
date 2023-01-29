package com.umc.pureum.domain.sentence.entity;

import com.sun.istack.NotNull;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Setter
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class Sentence extends BaseEntity {
    @ManyToOne @JoinColumn(name = "user_id")
    private UserAccount user;
    @NotNull
    private String sentence;
    @ManyToOne @JoinColumn(name = "word_id")
    private Keyword keyword;
    @NotNull
    private String status;

    public Sentence(UserAccount userAccount, String sentence, Keyword keyword, String sentenceStatus) {
        this.user = userAccount;
        this.sentence = sentence;
        this.keyword = keyword;
        this.status = sentenceStatus;
    }
}
