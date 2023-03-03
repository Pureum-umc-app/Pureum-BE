package com.umc.pureum.domain.sentence.entity;

import com.sun.istack.NotNull;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Setter
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)

public class Sentence extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;
    @NotNull
    private String sentence;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Keyword keyword;
    @NotNull
    private String status;

    public Sentence(UserAccount userAccount, String sentence, Keyword keyword, String sentenceStatus) {
        this.user = userAccount;
        this.sentence = sentence;
        this.keyword = keyword;
        this.status = sentenceStatus;
    }

    // 문장 수정
    public void EditSentence(String sentence){
        this.sentence = sentence;
    }

    // 문장 삭제
    public void DeleteSentence(String sentenceStatus){
        this.status = sentenceStatus;
    }
}
