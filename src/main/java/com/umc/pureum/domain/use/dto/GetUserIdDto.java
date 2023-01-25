package com.umc.pureum.domain.use.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetUserIdDto {

    private Long id;

    @Builder
    public GetUserIdDto(Long id){
        this.id = id;
    }
}
