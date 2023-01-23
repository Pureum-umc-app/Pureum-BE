package com.umc.pureum.domain.user.entity;

import com.umc.pureum.global.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

/**
 * 유저 entity
 */
@DynamicInsert
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
    private int grade; //학년
    private String image; // 프로필 사진
    private String status; // A:active D:delete
}
