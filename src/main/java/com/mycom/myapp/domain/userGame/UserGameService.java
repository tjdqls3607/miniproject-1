package com.mycom.myapp.domain.userGame;

import org.springframework.stereotype.Service;

import com.mycom.myapp.common.auth.JwtTokenProvider;
import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.entity.UserGame;
import com.mycom.myapp.common.enums.MatchStatus;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.common.error.exceptions.NotFoundException;
import com.mycom.myapp.domain.game.GameRepository;
import com.mycom.myapp.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserGameService {
    private final UserGameRepository userGameRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final JwtTokenProvider jwtTokenProvider;
    
    public void participateGame(Long gameId) {
    	User user = jwtTokenProvider.getUserFromSecurityContext();
//    	User user = userRepository.findById(11L)
//    			.orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_USER));
    	Long userId = user.getId();
    	Game game = gameRepository.findById(gameId)
    			.orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_GAME));
    	
    	if(userGameRepository.existsByUserIdAndGameId(userId, gameId)) {
    		throw new IllegalStateException("이미 신청한 게임입니다.");
    	}
    	
    	UserGame userGame = UserGame.builder()
    			.user(user)
    			.game(game)
    			.matchStatus(MatchStatus.COMPLETED)
    			.build();
    	
    	userGameRepository.save(userGame);
    }
}
