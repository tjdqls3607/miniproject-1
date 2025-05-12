package com.mycom.myapp.domain.game;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycom.myapp.common.ResponseDTO;
import com.mycom.myapp.common.auth.JwtTokenProvider;
import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.domain.game.dto.GameCreateRequest;
import com.mycom.myapp.domain.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/game")
@Slf4j
public class GameController {
    private final GameService gameService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    @PostMapping
    public ResponseEntity<ResponseDTO<Game>> createGame(@RequestBody @Valid GameCreateRequest request) {
        User user = jwtTokenProvider.getUserFromSecurityContext();
        Game game = Game.builder()
                .location(request.getLocation())
                .time(request.getTime())
                .deadline(request.getDeadline())
                .participantMin(request.getParticipantMin())
                .participantMax(request.getParticipantMax())
                .againstPeople(request.getAgainstPeople())
                .gameInfo(request.getGameInfo())
                .gameNoti(request.getGameNoti())
                .host(user)
                .build();

        game = gameService.save(game);

        return ResponseEntity.status(201).body(ResponseDTO.success(ResponseCode.CREATED, Game.builder()
                        .id(game.getId())
                .location(game.getLocation())
                .time(game.getTime())
                .deadline(game.getDeadline())
                .participantMin(game.getParticipantMin())
                .participantMax(game.getParticipantMax())
                .againstPeople(game.getAgainstPeople())
                .gameInfo(game.getGameInfo())
                .gameNoti(game.getGameNoti())
                .build()
        ));
    }
	
	@GetMapping("/list")
	public List<GameDto> listGame(){
		log.debug("list 출력");
        return gameService.listGame(); 
	}
	
	@GetMapping("/detail/{gameId}")
	public GameDto detailGame(@PathVariable("gameId") Long gameId){
		log.debug("detail 출력");
		return gameService.detailGame(gameId);
	}
	
}
