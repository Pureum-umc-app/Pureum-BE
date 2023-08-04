package com.umc.pureum.domain.sentence.entity.mapping;


import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface SentenceLikeMapping {
    long getSentence_id();
    String getSentence();
    int getLikeNum();
    long getKeywordId();
    long getUserId();
    String getImage();
    String getKeyword();
    Timestamp getTime();
    String getNickname();
    int getBlameNum();
}
