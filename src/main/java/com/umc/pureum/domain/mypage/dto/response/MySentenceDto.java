package com.umc.pureum.domain.mypage.dto.response;


import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MySentenceDto {

    private Long sentenceId; // 문장 인덱슨
    private String word; // 단어
    private String sentence; // 문장
    private int countLike; // 좋아요 수
    private String status; // 공개여부


}
