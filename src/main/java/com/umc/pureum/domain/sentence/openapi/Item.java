package com.umc.pureum.domain.sentence.openapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private String link;
    private String pos;
    private List<Sense> sense;
    private int sup_no;
    private int target_code;
    private String word;
}
