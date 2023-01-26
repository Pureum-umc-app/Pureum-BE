package com.umc.pureum.domain.sentence.dto;

import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Sentence;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateSentenceReq {
    private Long kakaoId;
    private String sentence;
    private Keyword keyword;
}
