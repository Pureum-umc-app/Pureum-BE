package com.umc.pureum.domain.user.entity;

import com.umc.pureum.domain.attendance.entity.AttendanceCheck;
import com.umc.pureum.domain.badge.entity.Badge;
import com.umc.pureum.domain.battle.entity.Battle;
import com.umc.pureum.domain.battle.entity.BattleLike;
import com.umc.pureum.domain.battle.entity.BattleResult;
import com.umc.pureum.domain.battle.entity.BattleSentence;
import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.entity.SentenceLike;
import com.umc.pureum.domain.use.entity.UsePhone;
import com.umc.pureum.global.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
    private Long kakaoId;
    private String fcmId;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    @ToString.Exclude
    private List<UsePhone> usePhones = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    @ToString.Exclude
    private List<Sentence> sentences = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    @ToString.Exclude
    private List<SentenceLike> sentenceLikes = new ArrayList<>();

    @OneToMany(mappedBy = "challenger")
    @Builder.Default
    @ToString.Exclude
    private List<Battle> challengerBattleList = new ArrayList<>();

    @OneToMany(mappedBy = "challenged")
    @Builder.Default
    @ToString.Exclude
    private List<Battle> challengedBattleList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    @ToString.Exclude
    private List<BattleLike> battleLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    @ToString.Exclude
    private List<BattleResult> battleResults = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    @ToString.Exclude
    private List<BattleSentence> battleSentences = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    @ToString.Exclude
    private List<Badge> badges = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    @ToString.Exclude
    private List<AttendanceCheck> attendanceChecks = new ArrayList<>();
}
