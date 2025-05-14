package com.mycom.myapp.domain.game;

import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.domain.game.dto.GameDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Test
    void listGame() {
        // given
        LocalDateTime now = LocalDateTime.now();

        Game validGame = Game.builder()
                .id(1L)
                .location("서울")
                .time(now.plusDays(1))
                .deadline(now.plusHours(1))
                .participantMax(10)
                .participantMin(5)
                .againstPeople("5:5")
                .gameInfo("테스트 코드")
                .gameNoti("테스트 합니다.")
                .build();

        Game expiredGame = Game.builder()
                .id(2L)
                .location("부산")
                .time(now.plusDays(1))
                .deadline(now.minusHours(1))
                .participantMin(3)
                .participantMax(6)
                .againstPeople("6:6")
                .gameInfo("종료된 경기")
                .gameNoti("테스트입니다")
                .build();

        when(gameRepository.findAll()).thenReturn(List.of(validGame,expiredGame));

        List<GameDto> result = gameService.listGame();

        assertEquals(1, result.size());
        assertEquals(validGame.getId(), result.get(0).getId());
        assertEquals("서울", result.get(0).getLocation());

        verify(gameRepository, times(1)).findAll();

    }

    @Test
    void detailGame() {
        long gameId = 1L;
        Game game = new Game();

        game.setId(gameId);
        game.setLocation("서울");
        game.setTime(LocalDateTime.of(2025, 5, 20, 18, 0));
        game.setDeadline(LocalDateTime.of(2025, 5, 18, 23, 59));
        game.setParticipantMax(10);
        game.setParticipantMin(5);
        game.setAgainstPeople("5:5");
        game.setGameInfo("테스트 코드");
        game.setGameNoti("게임디테일 테스트코드");

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        GameDto result = gameService.detailGame(gameId);

        assertEquals(gameId, result.getId());
        assertEquals("서울", result.getLocation());
        assertEquals(LocalDateTime.of(2025, 5, 20, 18, 0), result.getTime());
        assertEquals(LocalDateTime.of(2025, 5, 18, 23, 59), result.getDeadline());
        assertEquals(10, result.getParticipantMax());
        assertEquals(5, result.getParticipantMin());
        assertEquals("5:5", result.getAgainstPeople());
        assertEquals("테스트 코드", result.getGameInfo());
        assertEquals("게임디테일 테스트코드", result.getGameNoti());

    }

}


