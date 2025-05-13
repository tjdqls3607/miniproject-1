package com.mycom.myapp.domain.userGame;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.mycom.myapp.common.auth.JwtTokenProvider;
import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.entity.UserGame;
import com.mycom.myapp.common.enums.MatchStatus;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.common.error.exceptions.NotFoundException;
import com.mycom.myapp.domain.game.GameRepository;
import com.mycom.myapp.domain.user.UserRepository;
import com.mycom.myapp.domain.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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

    @Test   // 매칭 조회 테스트
    void matching_check () {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);


        MatchStatus matchStatus = MatchStatus.COMPLETED;
        LocalDateTime after = LocalDateTime.of(2025, 5, 1, 0, 0);
        LocalDateTime before = LocalDateTime.of(2025, 5, 31, 23, 59);

        UserGameDto dto1 = new UserGameDto(1L, "서울", LocalDateTime.of(2025, 5, 10, 15, 0), "5:5",11L);
        UserGameDto dto2 = new UserGameDto(2L, "부산", LocalDateTime.of(2025, 6, 1, 15, 0), "6:6", 11L);

        List<UserGameDto> dtos = List.of(dto1, dto2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userGameRepository.findDtoByUserAndMatchStatus(mockUser, matchStatus)).thenReturn(dtos);

        List<UserGameDto> result = userGameService.getMyParticipations(userId, matchStatus, after, before);

        assertEquals(1, result.size());
        assertEquals("서울", result.get(0).getLocation());
        assertTrue(result.get(0).getTime().isAfter(after));
        assertTrue(result.get(0).getTime().isBefore(before));



    }

    @Test
    void getMyCreatedMatches() {
        Long userId = 1L;

        User host = new User();
        host.setId(userId);

        Game game1 = new Game();
        game1.setId(100L);
        game1.setLocation("서울");
        game1.setTime(LocalDateTime.of(2025, 5, 20, 15, 0));
        game1.setAgainstPeople("10");
        game1.setHost(host);

        Game game2 =new Game();
        game2.setId(200L);
        game2.setLocation("부산");
        game2.setTime(LocalDateTime.of(2025, 5, 22, 16, 0));
        game2.setAgainstPeople("12");
        game2.setHost(host);

        List<Game> games = List.of(game1, game2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(host));
        when(gameRepository.findByHost(host)).thenReturn(games);

        List<UserGameDto> result = userGameService.getMyCreatedMatches(userId);

        assertEquals(2, result.size());

        assertEquals(game1.getId(), result.get(0).getId());
        assertEquals(game1.getLocation(), result.get(0).getLocation());
        assertEquals(game1.getTime(), result.get(0).getTime());
        assertEquals(game1.getAgainstPeople(), result.get(0).getAgainstPeople());
        assertEquals(userId, result.get(0).getHostId());

        assertEquals(game2.getId(), result.get(1).getId());


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
