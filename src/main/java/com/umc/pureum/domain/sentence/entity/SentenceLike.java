package com.umc.pureum.domain.sentence.entity;

import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class SentenceLike extends BaseEntity {
    @ManyToOne @JoinColumn(name = "user_id")
    private UserAccount user;
    @ManyToOne @JoinColumn(name = "sentence_id")
    private Sentence sentence;
    private String status;

    @Builder
    public SentenceLike(UserAccount user, Sentence sentence, String status) {
        this.user = user;
        this.sentence = sentence;
        this.status = status;
    }
}
