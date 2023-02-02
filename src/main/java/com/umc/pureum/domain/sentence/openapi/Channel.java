package com.umc.pureum.domain.sentence.openapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel {
    private String description;
    private List<Item> item;
    private Long lastBuildDate;
    private String link;
    private int num;
    private int start;
    private String title;
    private int total;
}
