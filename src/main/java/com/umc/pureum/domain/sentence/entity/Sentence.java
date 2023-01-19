package com.umc.pureum.domain.sentence.entity;

import com.sun.istack.NotNull;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import com.umc.pureum.global.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

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

    @Builder
    public Sentence(UserAccount user, String sentence, Keyword word, String status) {
        this.user = user;
        this.sentence = sentence;
        this.keyword = word;
        this.status = status;
    }
}
