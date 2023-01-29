package com.umc.pureum.domain.mypage.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetMySentencesRes<T> {

    private int count; // 작성한 문장 개수
    private int countOpen;  // 공개한 문장 개수
    private T mySentence; // 나의 문장 리스트
}
