package com.umc.pureum.domain.sentence.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeSentenceReq {
    private Long sentenceId;

    public LikeSentenceReq() {
    }
}
