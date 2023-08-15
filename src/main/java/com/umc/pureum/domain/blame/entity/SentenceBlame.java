package com.umc.pureum.domain.blame.entity;

import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.BaseEntity;
import com.umc.pureum.global.entity.Status;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Getter
@Entity
@Table(name = "sentence_blame")
public class SentenceBlame extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sentence_id")
    Sentence sentence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    UserAccount user;
    @Enumerated(EnumType.STRING)
    Status status;

    public void updateState(Status status) {
        this.status = status;
    }

    public void setUser(UserAccount userAccount) {
        this.user = userAccount;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

}
