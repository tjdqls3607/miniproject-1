package com.mycom.myapp.domain.userGame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

import com.mycom.myapp.common.auth.JwtTokenProvider;
import com.mycom.myapp.domain.game.GameRepository;
import com.mycom.myapp.domain.user.UserRepository;
@ExtendWith(MockitoExtension.class)
public class UserGameServiceTest {
	@InjectMocks
	private UserGameService userGameService;
	
	@Mock
	private UserGameRepository userGameRepository;

	@Mock
    private UserRepository userRepository;

	@Mock
    private GameRepository gameRepository;

	@Mock
    private JwtTokenProvider jwtTokenProvider;
	
	@Test
	@DisplayName("게임 참여 성공")
	void participateGameSuccess() {
		Long gameId = 1L;
        User mockUser = User.builder().id(1L).build();
	}
}
