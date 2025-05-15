package com.mycom.myapp.domain.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.common.error.exceptions.NotFoundException;
import com.mycom.myapp.domain.game.dto.GameDto;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    void saveGame() {
        // Given: 저장할 게임 객체
        Game game = new Game();
        game.setLocation("서울 월드컵 경기장");

        // When: save 호출
        when(gameRepository.save(any(Game.class))).thenReturn(game); // gameRepository.save가 호출되면 game을 반환하도록 mock 설정

        // Then: GameService save 메서드 호출
        Game savedGame = gameService.save(game);

        // 검증: gameRepository.save가 호출되었는지 확인
        verify(gameRepository, times(1)).save(game);

        // 또한 save 메서드가 반환하는 객체가 예상대로인지 확인
        assertEquals(game, savedGame);
    }
    
    @Test
    void listGame_Deadline() {
        // Given
        // 마감시간 남은 game1
        Game game1 = new Game();
        game1.setId(1L);
        game1.setLocation("서울 월드컵 경기장");
        game1.setDeadline(LocalDateTime.now().plusDays(1));
        game1.setTime(LocalDateTime.now().plusDays(2));

        // 마감시간 지난 game2
        Game game2 = new Game();
        game2.setId(2L);
        game2.setLocation("대전 월드컵 경기장");
        game2.setDeadline(LocalDateTime.now().minusDays(1));
        game2.setTime(LocalDateTime.now().minusDays(2));

        List<Game> games = List.of(game1, game2);

        when(gameRepository.findAll()).thenReturn(games);

        // When
        List<GameDto> result = gameService.listGame();

        // Then
        // 마감 시간 남은 게임만 반환
        assertEquals(1, result.size());
        assertEquals(game1.getId(), result.get(0).getId());
    }

    @Test
    void listGame_DeadlineNow() {
        Game game1 = new Game();
        game1.setId(1L);
        game1.setDeadline(LocalDateTime.now()); // 마감 시간이 현재

        when(gameRepository.findAll()).thenReturn(List.of(game1));

        List<GameDto> result = gameService.listGame();

        assertEquals(0, result.size()); // 마감 시간이 지나지 않은 게임만 포함
    }

    @Test
    void detailGameExist() {
        Game game = new Game();
        game.setId(1L);
        game.setLocation("서울 월드컵 경기장");
        game.setDeadline(LocalDateTime.now().plusDays(1));

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        GameDto result = gameService.detailGame(1L);

        verify(gameRepository, times(1)).findById(1L);

        assertEquals(game.getId(), result.getId());
        assertEquals(game.getLocation(), result.getLocation());
    }

    @Test
    void detailGameNotExist() {
        when(gameRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> gameService.detailGame(1L));

        assertEquals(ResponseCode.NOT_FOUND_GAME.getCode(), exception.getResponseCode().getCode());
    }

    @Test
    void searchGameLocation_Success() {        
    	// Given
        // 마감시간 남은 game1
        Game game1 = new Game();
        game1.setId(1L);
        game1.setLocation("서울 월드컵 경기장");
        game1.setDeadline(LocalDateTime.now().plusDays(1));
        game1.setTime(LocalDateTime.now().plusDays(2));

        // 마감시간 지난 game2
        Game game2 = new Game();
        game2.setId(2L);
        game2.setLocation("서울 송파구 잠실 경기장");
        game2.setDeadline(LocalDateTime.now().minusDays(1));
        game2.setTime(LocalDateTime.now().minusDays(2));

        List<Game> games = List.of(game1, game2);

        when(gameRepository.findByLocationLike("%서울%")).thenReturn(games);

        // When
        List<GameDto> result = gameService.searchGameLocation("서울");

        // Then
        // 마감 시간 남은 게임만 반환
        assertEquals(1, result.size());
        assertEquals(game1.getId(), result.get(0).getId());
    }

}

