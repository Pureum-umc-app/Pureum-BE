package com.umc.pureum.domain.mypage.dto;


import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MySentenceDto {

    private String word; // 단어
    private String sentence; // 문장
    private int countLike; // 좋아요 수
    private String status; // 공개여부


}
