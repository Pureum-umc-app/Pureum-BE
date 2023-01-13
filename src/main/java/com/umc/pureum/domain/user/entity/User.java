package com.umc.pureum.domain.user.entity;

import com.umc.pureum.global.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class User extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;

    private String nickname;

    private String introduction;

    private String email;

    private int grade;

    private String image;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
}
