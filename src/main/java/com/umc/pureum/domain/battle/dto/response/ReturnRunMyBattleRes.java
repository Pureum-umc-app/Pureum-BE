package com.umc.pureum.domain.battle.dto.response;

import com.umc.pureum.domain.battle.entity.BattleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRunMyBattleRes {

    private Long battleId;
    private Long keywordId;
    private String keyword;
    private String remainDuration;
    private Long myId;
    private String myNickname;
    private String myImage;
    private Long oppId;
    private String oppNickname;
    private String oppImage;
    private int duration;
    private BattleStatus status;

    private Long mySentenceId;
    private String mySentence;
    private Long oppSentenceId;
    private String oppSentence;

    private Long myLikeCnt;
    private Long oppLikeCnt;

    private int myLike;
    private int oppLike;

}
