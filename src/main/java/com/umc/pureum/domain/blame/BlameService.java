package com.umc.pureum.domain.blame;

import com.umc.pureum.global.config.Response.BaseException;
import org.springframework.security.core.Authentication;

public interface BlameService {
    void battleSentenceBlame(long userId, long battleSentenceId) throws BaseException;
}
