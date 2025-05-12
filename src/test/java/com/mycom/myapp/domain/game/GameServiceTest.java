package com.mycom.myapp.domain.game;

import com.mycom.myapp.common.entity.Game;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
}
