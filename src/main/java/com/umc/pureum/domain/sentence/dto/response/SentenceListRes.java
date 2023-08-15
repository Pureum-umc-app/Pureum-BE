package com.umc.pureum.domain.sentence.dto.response;

public interface SentenceListRes {
    Long getSentenceId();
    String getSentence();
    Long getKeywordId();
    String getKeyword();
    Long getUserId();
    String getNickname();
    String getProfileImg();
    String getDate();
    int getLikeCnt();
    String getIsLiked();
    String getIsBlamed();
}
