package com.mycom.myapp.domain.game.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Data
@Builder
public class GameCreateRequest {

    @NotBlank(message = "Location is required.")
    private String location;  // 장소는 필수

    @NotNull(message = "Game time is required.")
    private LocalDateTime time;  // 게임 시간은 필수

    @NotNull(message = "Deadline is required.")
    private LocalDateTime deadline;  // 마감 시간은 필수

    @NotNull(message = "Minimum participants are required.")
    private int participantMin;  // 최소 참가자 수는 필수

    @NotNull(message = "Maximum participants are required.")
    private int participantMax;  // 최대 참가자 수는 필수

    @Size(max = 500, message = "Against people description should not exceed 500 characters.")
    private String againstPeople;  // 상대팀 설명 (500자 이하)

    @Size(max = 1000, message = "Game information should not exceed 1000 characters.")
    private String gameInfo;  // 게임 정보 (1000자 이하)

    @Size(max = 500, message = "Game notification should not exceed 500 characters.")
    private String gameNoti;  // 게임 공지 (500자 이하)
}