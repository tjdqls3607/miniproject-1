package com.mycom.myapp.domain.userGame;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.mycom.myapp.common.auth.JwtTokenProvider;
import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.entity.UserGame;
import com.mycom.myapp.common.enums.MatchStatus;
import com.mycom.myapp.domain.game.GameRepository;
import com.mycom.myapp.domain.user.UserRepository;
import com.mycom.myapp.domain.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.mycom.myapp.common.enums.MatchStatus.COMPLETED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
public class UserGameServiceTest {

    @InjectMocks
    private UserGameService userGameService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserGameRepository userGameRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;


    @Test   // 매칭 취소(주최자)
    void testDeleteMatch_HOST_success() {
        // given
        Long hostId = 1L;
        Long gameId = 100L;

        User host = new User();
        host.setId(hostId);

        Game game = new Game();
        game.setId(gameId);
        game.setHost(host);

        UserGame ug1 = new UserGame();
        UserGame ug2 = new UserGame();

        when(jwtTokenProvider.getUserFromSecurityContext()).thenReturn(host);   // 매칭삭제를 jwt로 받고있기 때문
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(userGameRepository.findByGame(game)).thenReturn(List.of(ug1, ug2));

        //when
        userGameService.deleteMatch(gameId);

        // then
        verify(userGameRepository).deleteAll(List.of(ug1, ug2));
        verify(gameRepository).delete(game);
    }

    @Test   // 매칭 취소(참가자)
    void testCancelParticipation_softDelete() {
        Long gameId = 200L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);

        User host = new User();
        host.setId(1L);

        Game game = new Game();
        game.setId(gameId);
        game.setHost(host);

        UserGame userGame = new UserGame();
        userGame.setGame(game);
        userGame.setUser(user);
        userGame.setMatchStatus(MatchStatus.COMPLETED);

        when(jwtTokenProvider.getUserFromSecurityContext()).thenReturn(user);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(userGameRepository.findByUserAndGame(user, game)).thenReturn(Optional.of(userGame));

        //when
        userGameService.cancelParticipation(gameId);

        //then
        assertEquals(MatchStatus.CANCELLED, userGame.getMatchStatus());
    }

    @Test   // 참가자가 경기취소(주최자) 취소 테스트
    void testCancelParticipation_fail_whenHostTriesToCancel() {
        Long gameId = 1L;
        Long userId = 10L;

        User host = new User();
        host.setId(userId);

        Game game = new Game();
        game.setId(gameId);
        game.setHost(host);

        when(jwtTokenProvider.getUserFromSecurityContext()).thenReturn(host);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userGameService.cancelParticipation(gameId);
        });

        assertEquals("주최자는 참가 취소가 불가능 합니다.", ex.getMessage());


    }
}
