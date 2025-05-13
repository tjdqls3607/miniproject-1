package com.mycom.myapp.domain.userGame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mycom.myapp.common.auth.JwtTokenProvider;
import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.entity.UserGame;
import com.mycom.myapp.common.enums.MatchStatus;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.common.error.exceptions.NotFoundException;
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

        Game game = new Game();
        game.setId(gameId);

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
    
    @Test
    @DisplayName("게임 참여 성공")
    void participateGame_Success() {
        Long gameId = 1L;
        User mockUser = User.builder().id(1L).build();
        Game mockGame = Game.builder().id(gameId).build();
        
        when(jwtTokenProvider.getUserFromSecurityContext()).thenReturn(mockUser);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));
        when(userGameRepository.existsByUserIdAndGameId(mockUser.getId(), gameId)).thenReturn(false);

        userGameService.participateGame(gameId);

        verify(userGameRepository, times(1)).save(any(UserGame.class));
    }
    
    @Test
    @DisplayName("게임 참여 실패 - 존재하지 않는 게임")
    void participateGame_NotFoundGame() {
    	Long gameId = 1L;
        when(jwtTokenProvider.getUserFromSecurityContext()).thenReturn(User.builder().id(11L).build());
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, 
        		() -> userGameService.participateGame(gameId));

        assertEquals(ResponseCode.NOT_FOUND_GAME.getCode(), exception.getResponseCode().getCode());
    }
    
    @Test
    @DisplayName("게임 참여 실패 - 이미 참여한 게임")
    void participateGame_AlreadyParticipated() {
        Long gameId = 1L;
        User mockUser = User.builder().id(1L).build();
        Game mockGame = Game.builder().id(gameId).build();

        when(jwtTokenProvider.getUserFromSecurityContext()).thenReturn(mockUser);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));
        when(userGameRepository.existsByUserIdAndGameId(mockUser.getId(), gameId)).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, 
        		() -> userGameService.participateGame(gameId));
        assertEquals("이미 신청한 게임입니다.", exception.getMessage());
    }
}
