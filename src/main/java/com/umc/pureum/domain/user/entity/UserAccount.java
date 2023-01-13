package com.umc.pureum.domain.user.entity;

import com.umc.pureum.global.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@ToString
public class UserAccount extends BaseEntity {
    private String name;
    private String nickname;
    private String introduction;
    private String email;
    private int grade;
    private String image;
    private String status; // A:active D:delete
}
