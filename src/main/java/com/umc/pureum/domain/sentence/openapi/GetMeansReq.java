package com.umc.pureum.domain.sentence.openapi;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetMeansReq {
    private String key;
    private String q;
    private String part;
    private String translated;
    private String method;

    public GetMeansReq(String key, String q) {
        this.key = key;
        this.q = q;
        this.part = "word";
        this.translated = "n";
        this.method = "exact";
    }

    public String getParameter() {
        return "?key=" + this.key +
                "&q=" + this.q +
                "&part=" + this.part +
                "&translated=" + this.translated +
                "&method=" + this.method;
    }
}
