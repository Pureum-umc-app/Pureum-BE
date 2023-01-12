package com.umc.pureum.domain.sentence.entity;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class SentenceLike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne @JoinColumn(name = "sentence_id")
    private Sentence sentence;
    private String status;

    @Builder
    public Sentence(Long id, User user, Sentence sentence, Keyword word, String status) {
        this.id = id;
        this.user = user;
        this.sentence = sentence;
        this.status = status;
    }
}
