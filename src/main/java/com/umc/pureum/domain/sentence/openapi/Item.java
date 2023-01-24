package com.umc.pureum.domain.sentence.openapi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Item {
    private String link;
    private String origin;
    private String pos;
    private String pronunciation;
    private List<Sense> sense;
    private int sup_no;
    private int target_code;
    private String word;
    private String word_grade;
}
