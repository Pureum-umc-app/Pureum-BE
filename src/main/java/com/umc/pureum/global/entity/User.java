package com.umc.pureum.global.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
@Entity
@Getter
@Setter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String nickname;
    private String introduction;
    private String email;
    private int grade;
    private String image;
    private Timestamp create_id;
    private Timestamp update_id;
    private String status; // A:active D:delete
}
