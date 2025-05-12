package com.mycom.myapp.domain.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycom.myapp.common.ResponseDTO;
import com.mycom.myapp.common.auth.JwtTokenProvider;
import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.common.error.exceptions.UnauthorizedException;
import com.mycom.myapp.domain.game.dto.GameCreateRequest;
import com.mycom.myapp.domain.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

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
}
