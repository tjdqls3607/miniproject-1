package com.mycom.myapp.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "Game")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Game extends BaseEntity{
    @Column(nullable = false, length = 100)
    private String location; // 경기 장소

    @Column(nullable = false)
    private LocalDateTime time; // 경기 시간

    @Column(nullable = false)
    private LocalDateTime deadline; // 마감 시간

    @Column(nullable = false)
    private int participantMin; // 최소 참가자 수

    @Column(nullable = false)
    private int participantMax; // 최대 참가자 수

    @Column(nullable = false)
    private String againstPeople; // 몇 대 몇 경기 (예: "5:5")

    @Column(columnDefinition = "TEXT")
    private String gameInfo; // 구장 정보

    @Column(columnDefinition = "TEXT")
    private String gameNoti; // 구장 안내사항

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User host; // 매치 주최자
}
