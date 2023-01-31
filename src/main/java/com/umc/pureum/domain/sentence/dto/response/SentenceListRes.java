package com.umc.pureum.domain.sentence.dto.response;

import com.umc.pureum.domain.sentence.entity.mapping.SentenceLikeMapping;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SentenceListRes {
    private long sentenceId;
    private String sentence;
    private long keywordId;
    private String keyword;
    private long userId;
    private String nickname;
    private String image;
    private String time;
    private int likeNum;
    private boolean selfLike;


}
