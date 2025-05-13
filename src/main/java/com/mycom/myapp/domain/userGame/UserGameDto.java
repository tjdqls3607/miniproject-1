package com.mycom.myapp.domain.userGame;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserGameDto {
    private Long id;
    private String location;
    private LocalDateTime time;
    private String againstPeople;
    private Long hostId;
}