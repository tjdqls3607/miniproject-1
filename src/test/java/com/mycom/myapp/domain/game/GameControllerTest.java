package com.mycom.myapp.domain.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.mycom.myapp.common.ResponseDTO;
import com.mycom.myapp.common.auth.JwtTokenProvider;
import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.common.error.exceptions.NotFoundException;
import com.mycom.myapp.common.error.exceptions.UnauthorizedException;
import com.mycom.myapp.domain.game.dto.GameCreateRequest;
import com.mycom.myapp.domain.game.dto.GameDto;
import com.mycom.myapp.domain.user.UserService;
import com.mycom.myapp.domain.userGame.UserGameService;
@ExtendWith(MockitoExtension.class)
public class GameControllerTest {
    @InjectMocks
    private GameController gameController;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private GameService gameService;

    @Mock
    private UserService userService;

    @Mock
    private UserGameService userGameService;

    @Test
    @DisplayName("게임 생성 성공")
    void createGameSuccess() {
        GameCreateRequest request = GameCreateRequest.builder()
                .location("서울 월드컵 경기장")
                .time(LocalDateTime.now().plusDays(1))
                .deadline(LocalDateTime.now().plusHours(12))
                .participantMin(5)
                .participantMax(10)
                .againstPeople("무적 FC")
                .gameInfo("친선 경기")
                .gameNoti("시간 엄수")
                .build();

        User mockUser = User.builder().id(1L).email("test@example.com").build();
        Game mockGame = Game.builder().id(1L).location(request.getLocation()).build();

        when(jwtTokenProvider.getUserFromSecurityContext()).thenReturn(mockUser);
        when(gameService.save(any(Game.class))).thenReturn(mockGame);

        ResponseEntity<ResponseDTO<Game>> response = gameController.createGame(request);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(ResponseCode.CREATED.getCode(), Objects.requireNonNull(response.getBody()).getCode());
    }

    @Test
    @DisplayName("게임 생성 실패 - 인증되지 않은 사용자")
    void createGameFail_Unauthorized() {
        GameCreateRequest request = GameCreateRequest.builder()
                .location("서울 월드컵 경기장")
                .time(LocalDateTime.now().plusDays(1))
                .deadline(LocalDateTime.now().plusHours(12))
                .participantMin(5)
                .participantMax(10)
                .againstPeople("무적 FC")
                .gameInfo("친선 경기")
                .gameNoti("시간 엄수")
                .build();

        // jwtTokenProvider가 UnauthorizedException을 던지도록 설정
        when(jwtTokenProvider.getUserFromSecurityContext()).thenThrow(new UnauthorizedException(ResponseCode.INVALID_TOKEN));
        // 게임 생성 요청 (여기서 UnauthorizedException이 발생해야 함)
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            gameController.createGame(request);
        });
        // 예외 발생 후 401 상태 코드가 반환되어야 함
        assertEquals(ResponseCode.INVALID_TOKEN.getCode(), exception.getResponseCode().getCode());
    }
    
    @Test
    void listGameSuccess() {
    	// Given: Mock된 게임 목록
        GameDto game1 = GameDto.builder()
                .id(1L)
                .location("서울 월드컵 경기장")
                .time(LocalDateTime.now().plusDays(1))
                .deadline(LocalDateTime.now().plusHours(12))
                .participantMin(5)
                .participantMax(10)
                .againstPeople("무적 FC")
                .gameInfo("친선 경기")
                .gameNoti("시간 엄수")
                .build();

        GameDto game2 = GameDto.builder()
                .id(2L)
                .location("부산 아시아드 경기장")
                .time(LocalDateTime.now().plusDays(2))
                .deadline(LocalDateTime.now().plusHours(24))
                .participantMin(6)
                .participantMax(12)
                .againstPeople("부산 유나이티드")
                .gameInfo("리그 경기")
                .gameNoti("경기장 규정 준수")
                .build();
        
        List<GameDto> mockGames = List.of(game1, game2);
        when(gameService.listGame()).thenReturn(mockGames);

        // When: listGame 호출
        List<GameDto> result = gameController.listGame();

        // Then: 반환 값 검증
        assertThat(result).containsExactlyInAnyOrderElementsOf(mockGames);
    }
    
    @Test
    void detailGameSuccess() {
        // Given: 특정 게임의 상세 정보
        GameDto mockGame = GameDto.builder()
                .id(1L)
                .location("서울 월드컵 경기장")
                .time(LocalDateTime.now().plusDays(1))
                .deadline(LocalDateTime.now().plusHours(12))
                .participantMin(5)
                .participantMax(10)
                .againstPeople("무적 FC")
                .gameInfo("친선 경기")
                .gameNoti("시간 엄수")
                .build();

        when(gameService.detailGame(1L)).thenReturn(mockGame);

        // When: detailGame 호출
        GameDto result = gameController.detailGame(1L);

        // Then: 반환 값 검증
        assertEquals(1L, result.getId());
        assertEquals(mockGame, result);
    }

    @Test
    @DisplayName("게임 상세 조회 실패 - 게임이 존재하지 않음")
    void detailGameFail_NotFound() {
        // Given: gameService.detailGame 호출 시 예외 발생
        when(gameService.detailGame(999L)).thenThrow(new NotFoundException(ResponseCode.NOT_FOUND_GAME));

        // When: detailGame 호출 시 NotFoundException 발생 확인
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            gameController.detailGame(999L);
        });

        // Then: 예외 메시지 및 코드 검증
        assertEquals(ResponseCode.NOT_FOUND_GAME.getCode(), exception.getResponseCode().getCode());
    }

}
