package com.mycom.myapp.common.entity;

import com.mycom.myapp.common.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "UserGame")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UserGame extends BaseEntity{
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus matchStatus; // 참여 상태값 (COMPLETED, CANCELLED)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
}
