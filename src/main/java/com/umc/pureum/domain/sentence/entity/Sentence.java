package com.umc.pureum.domain.sentence.entity;

import com.sun.istack.NotNull;
import com.umc.pureum.domain.blame.entity.SentenceBlame;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    private String status;

    @OneToMany(mappedBy = "sentence")
    @Builder.Default
    @ToString.Exclude
    private List<SentenceBlame> sentenceBlameList = new ArrayList<>();

    public Sentence(UserAccount userAccount, String sentence, Keyword keyword, String status) {
        this.user = userAccount;
        this.sentence = sentence;
        this.keyword = keyword;
        this.status = status;
    }

    // 문장 수정
    public void EditSentence(String sentence){
        this.sentence = sentence;
    }

    // 문장 삭제
    public void DeleteSentence(String status){
        this.status = status;
    }

    public void addSentenceBlame(SentenceBlame sentenceBlame) {
        sentenceBlameList.add(sentenceBlame);
        sentenceBlame.setSentence(this);
    }

    public void updateStatus(String status) {
        this.status = status;
    }
}
