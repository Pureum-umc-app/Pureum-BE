package com.umc.pureum.domain.notification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDTO {
    String targetToken;
    String title;
    String body;
}
