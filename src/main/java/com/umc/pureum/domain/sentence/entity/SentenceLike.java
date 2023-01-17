package com.umc.pureum.domain.sentence.entity;

import com.sun.istack.NotNull;
import com.umc.pureum.global.entity.BaseEntity;
import com.umc.pureum.global.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class SentenceLike extends BaseEntity {
    @ManyToOne @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne @JoinColumn(name = "sentence_id")
    private Sentence sentence;
    private String status;

    @Builder
    public SentenceLike(User user, Sentence sentence, String status) {
        this.user = user;
        this.sentence = sentence;
        this.status = status;
    }
}
