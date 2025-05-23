package com.mycom.myapp.domain.game;

import java.util.List;

import com.mycom.myapp.common.ResponseDTO;
import com.mycom.myapp.common.auth.CustomUserDetails;
import com.mycom.myapp.common.auth.JwtTokenProvider;
import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.domain.game.dto.GameCreateRequest;
import com.mycom.myapp.domain.game.dto.GameDto;
import com.mycom.myapp.domain.user.UserService;
import com.mycom.myapp.domain.userGame.UserGameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/game")
@Slf4j
public class GameController {

    private final GameService gameService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserGameService userGameService;
    private final GameRepository gameRepository;

    /**
     * ✅ 공통 코드 포함 경기 생성
     */
    @PostMapping
    public ResponseEntity<?> createGameWithCommonCodes(@RequestBody GameDto dto,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        Game game = new Game();
        game.setLocation(dto.getLocation());
        game.setTime(dto.getTime());
        game.setDeadline(dto.getDeadline());
        game.setParticipantMin(dto.getParticipantMin());
        game.setParticipantMax(dto.getParticipantMax());
        game.setAgainstPeople(dto.getAgainstPeople());
        game.setGameInfo(dto.getGameInfo());
        game.setGameNoti(dto.getGameNoti());

        // ✅ 공통 코드 저장
        if (dto.getGameOptions() != null && !dto.getGameOptions().isEmpty()) {
            game.setGameOptions(String.join(",", dto.getGameOptions()));
        } else {
            game.setGameOptions(null);
        }

        // ✅ userId만 저장 (외래키 아님)
        game.setHost(userDetails.getUser());

        gameRepository.save(game);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("POST_OR_PATCH_SUCCESS", "경기 등록 성공"));
    }

    /**
     * ⚠️ 사용 안 하는 기본 createGame 버전 (jwtTokenProvider로 user 가져오는 방식)
     * 필요 없으면 삭제해도 됨
     */
    @PostMapping("/legacy")
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
        userGameService.participateGame(game.getId());

        return ResponseEntity.status(201).body(ResponseDTO.success(ResponseCode.CREATED, game));
    }

    /**
     * 경기 전체 목록
     */
    @GetMapping("/list")
    public List<GameDto> listGame() {
        log.debug("list 출력");
        return gameService.listGame();
    }

    /**
     * 경기 상세 보기
     */
    @GetMapping("/detail/{gameId}")
    public GameDto detailGame(@PathVariable("gameId") Long gameId) {
        log.debug("detail 출력");
        return gameService.detailGame(gameId);
    }

    /**
     * 장소로 경기 검색
     */
    @GetMapping("/search")
    public List<GameDto> searchGameLocation(@RequestParam("location") String location) {
        log.debug("search 출력");
        return gameService.searchGameLocation(location);
    }

    /**
     * 응답 포맷 클래스
     */
    record ApiResponse(String code, String message) {}
}
