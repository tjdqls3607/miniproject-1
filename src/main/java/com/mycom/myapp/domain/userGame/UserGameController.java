package com.mycom.myapp.domain.userGame;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycom.myapp.common.enums.ResponseCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-game")
@Slf4j
public class UserGameController {
	
	private final UserGameService userGameService;
	
	@PostMapping("/{gameId}/participate")
	public ResponseEntity<?> participateGame(@PathVariable("gameId") Long gameId){
		log.error("gameid=" + gameId);
		userGameService.participateGame(gameId);
		return ResponseEntity.ok(ResponseCode.SUCCESS);
	}
}
