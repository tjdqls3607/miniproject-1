package com.mycom.myapp.domain.game;

import java.time.LocalDateTime;

import com.mycom.myapp.common.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {
	private Long id; // 게임 id
    private String location; // 경기 장소
    private LocalDateTime time; // 경기 시간
    private LocalDateTime deadline; // 마감 시간
    private int participantMin; // 최소 참가자 수
    private int participantMax; // 최대 참가자 수
    private String againstPeople; // 몇 대 몇 경기 (예: "5:5")
    private String gameInfo; // 구장 정보
    private String gameNoti; // 구장 안내사항
    private User host; // 매치 주최자
}
