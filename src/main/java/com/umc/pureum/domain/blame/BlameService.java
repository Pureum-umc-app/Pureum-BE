package com.umc.pureum.domain.blame;

import com.umc.pureum.global.config.Response.BaseException;

public interface BlameService {
    boolean battleSentenceBlame(long userId, long battleSentenceId) throws BaseException;

    boolean sentenceBlame(long userId, long sentenceId) throws BaseException;
}
