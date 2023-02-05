package com.umc.pureum.domain.sentence.dto.request;

import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Sentence;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateSentenceReq {

    private Long userId;
    private Long keywordId;
    private String sentence;
    private String status;

    public CreateSentenceReq() {
    }
}
