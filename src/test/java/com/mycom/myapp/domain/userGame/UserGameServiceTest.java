package com.mycom.myapp.domain.userGame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.entity.UserGame;
import com.mycom.myapp.common.enums.MatchStatus;
import com.mycom.myapp.domain.game.GameRepository;
import com.mycom.myapp.domain.user.UserRepository;



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

    @Test
    void testCancelParticipation_success() {

        Long gameId = 1L;
        Long userId = 2L;

        User mockUser = new User();
        mockUser.setId(userId);

        Game mockGame = new Game();
        mockGame.setId(gameId);

        UserGame userGame = new UserGame();
        userGame.setGame(mockGame);
        userGame.setUser(mockUser);
        userGame.setMatchStatus(MatchStatus.COMPLETED);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userGameRepository.findByUserAndGame(mockUser, mockGame)).thenReturn(Optional.of(userGame));

        userGameService.cancelParticipation(gameId, userId);

        assertEquals(MatchStatus.CANCELLED, userGame.getMatchStatus());

    }

    @Test
    void testDeleteMatch_success() {
        // given
        Long hostId = 1L;
        Long gameId = 10L;

        User host = new User();
        host.setId(hostId);

        Game game = new Game();
        game.setId(gameId);
        game.setHost(host);
        game.setDeleted(false);

        UserGame ug1 = new UserGame();
        ug1.setMatchStatus(MatchStatus.COMPLETED);

        UserGame ug2 = new UserGame();
        ug2.setMatchStatus(MatchStatus.COMPLETED);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(userGameRepository.findByGame(game)).thenReturn(List.of(ug1, ug2));

        // when
        userGameService.deleteMatch(gameId, hostId);

        // then
        assertTrue(game.isDeleted());
        assertEquals(MatchStatus.CANCELLED, ug1.getMatchStatus());
        assertEquals(MatchStatus.CANCELLED, ug2.getMatchStatus());
    }
}
