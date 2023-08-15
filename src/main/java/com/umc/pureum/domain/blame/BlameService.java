package com.umc.pureum.domain.blame;

import com.umc.pureum.global.config.Response.BaseException;

public interface BlameService {
    boolean battleSentenceBlame(Long userId, Long battleSentenceId) throws BaseException;

    boolean sentenceBlame(Long userId, Long sentenceId) throws BaseException;

    boolean getSentenceSelfBlame(Long userId, Long id);
}
