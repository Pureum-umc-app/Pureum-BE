package com.umc.pureum.domain.inquiry.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostInquiryReq {
    String email;
    String content;
}
