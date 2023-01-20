package com.umc.pureum.global.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@ToString
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @CreatedDate @Column(name = "created_at")
    private Timestamp createdAt;
    @LastModifiedDate @Column(name = "updated_at")
    private Timestamp updatedAt;

}
