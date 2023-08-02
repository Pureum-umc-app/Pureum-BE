package com.umc.pureum.domain.inquiry.entity;

import com.umc.pureum.global.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

@NoArgsConstructor
@Data
@Entity
@SuperBuilder
@Setter
public class Inquiry extends BaseEntity {

    @NotNull
    @Column(name="inquiry_email")
    private String email;

    @Column(name="inquiry_content")
    private String content;

    public Inquiry(String email, String content){
        this.email = email;
        this.content = content;
    }
}
