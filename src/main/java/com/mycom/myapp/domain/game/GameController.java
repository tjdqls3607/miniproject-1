package com.mycom.myapp.domain.game;

import com.mycom.myapp.common.ResponseDTO;
import com.mycom.myapp.common.auth.JwtTokenProvider;
import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.common.error.exceptions.NotFoundException;
import com.mycom.myapp.domain.game.dto.GameCreateRequest;
import com.mycom.myapp.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
@Slf4j
public class GameController {
    private final GameService gameService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody @Valid GameCreateRequest request) {
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

        gameService.save(game);

        return ResponseEntity.ok(ResponseDTO.success(ResponseCode.CREATED, GameCreateRequest.builder()
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
}
