package com.mycom.myapp.domain.userGame;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserGameDto {
    private Long id;
    private String location;
    private LocalDateTime time;
    private String againstPeople;
    private Long hostId;
}